# Spring Hexagonal DDD Tutorial

A complete, hands-on walkthrough of **hexagonal architecture (ports & adapters)** combined with **Domain-Driven Design (DDD)**, built with **Spring Boot 3.5.x** (Spring Framework 6, Java 17) and organized into separate Maven modules that make the architectural boundaries physical - not just packages within a single module.

The data model starts from `spring-boot-tutorial` (`categories`/`products`/`customers`/`orders`), **enriched** to give the DDD concepts (aggregates, value objects, domain events) something real to express.

This document is the **complete specification** of the project: it is meant to be followed step by step to implement each module.

## Table of contents

- [Why this tutorial differs from the others](#why-this-tutorial-differs-from-the-others)
- [Domain-Driven Design & Hexagonal Architecture fundamentals](#domain-driven-design--hexagonal-architecture-fundamentals)
- [Enriched domain model (DDD)](#enriched-domain-model-ddd)
- [Tech stack](#tech-stack)
- [Data model](#data-model)
- [Module dependency rules](#module-dependency-rules)
- [Module structure](#module-structure)
- [Standard response format](#standard-response-format)
- [Branching strategy](#branching-strategy)
- [feature/domain](#featuredomain)
- [feature/application](#featureapplication)
- [feature/infrastructure](#featureinfrastructure)
- [feature/bootstrap](#featurebootstrap)
- [feature/arch-test](#featurearch-test)
- [Order of work](#order-of-work)
- [Code conventions](#code-conventions)
- [Concepts covered](#concepts-covered)
- [How to follow this tutorial](#how-to-follow-this-tutorial)

## Why this tutorial differs from the others

In `spring-boot-tutorial`, packages are organized **by technical layer** (`controller`, `service`, `repository`, `entity`), all inside the same Maven module - nothing technically stops a controller from injecting a `Repository` directly.

Here, the organization is **by architectural layer**, and each layer is a **separate Maven module**, which makes dependency violations **impossible to compile**, not merely discouraged by convention:

- The **domain** depends on nothing (not Spring, not JPA, not the web) - it is plain Java only, and it defines its own boundary: the **ports** (`port/in`, `port/out`), following Eric Evans' Repository pattern extended to every port, not just persistence
- The **application** layer orchestrates the domain by **implementing its inbound ports** (`port/in`) and calling its outbound ports (`port/out`), without knowing how those outbound ports will themselves be implemented
- The **infrastructure** layer provides the **adapters** (REST, JPA) that implement domain's outbound ports and call its inbound ports - it depends on application and domain, never the other way around
- The **bootstrap** module is the only one that knows about everyone: it assembles the runnable Spring Boot application
- The **arch-test** module checks these rules automatically with **ArchUnit**, on every build

## Domain-Driven Design & Hexagonal Architecture fundamentals

Explaining the architecture itself, not just how to lay out packages, is the actual goal of this tutorial. Hexagonal architecture (also known as **Ports & Adapters**, coined by Alistair Cockburn) provides the outside shape (any number of driving adapters on one side, any number of driven adapters on the other, domain in the middle), but the package layout in this project follows **Eric Evans' Domain-Driven Design** (*Domain-Driven Design: Tackling Complexity in the Heart of Software*, 2003) as its primary theoretical reference, extended consistently to every port, not just persistence:

- **Ports belong to the domain, because they are domain concepts**: Evans defines the Repository as an abstraction the domain itself needs ("give me the `Customer` with this id") - the interface is declared in the domain layer precisely so the domain can express what it needs without knowing how it is fulfilled. This project applies that same reasoning to every port, not only `port/out` (repositories, event publisher) but also `port/in` (use cases): both are part of the domain's own boundary, declared in `domain.port.in`/`domain.port.out`. The `application` layer does not declare any port - it only **implements** `port/in` and **calls** `port/out`, as a thin orchestration layer with no business rules of its own. This differs from the popular Spring/hexagonal convention (Tom Hombergs' *Get Your Hands Dirty on Clean Architecture*), which keeps both kinds of ports in `application` and reserves `domain` for the model alone - a valid alternative, but not the one chosen here.
- **The Dependency Inversion Principle, applied architecturally**: instead of business logic depending on infrastructure (a service calling a JPA repository directly), infrastructure depends on business logic. The domain defines an interface (a **port**) describing what it needs (`OrderRepositoryPort`); infrastructure provides an **adapter** implementing it (`OrderRepositoryAdapter`, backed by Spring Data JPA). The domain never imports the adapter - only the reverse is true. This is why `domain` compiles with zero Spring/JPA dependency even though it now hosts the ports: it is not a stylistic choice, it is the mechanism that makes the inversion real rather than aspirational (see [feature/arch-test](#featurearch-test) for how this is enforced, not just documented).
- **Ports: driving vs. driven**: a *driving* (or *primary*) port is how the outside world triggers the domain (`domain.port.in`, a use case interface implemented by an `application` service and called by a controller). A *driven* (or *secondary*) port is how the domain reaches out to the outside world (`domain.port.out`, an interface implemented by a persistence or messaging adapter). The hexagon shape is a metaphor, not a literal geometry.
- **Aggregate and Aggregate Root**: a cluster of domain objects treated as a single consistency boundary. `Order` is the Aggregate Root of `OrderLine`: nothing outside the aggregate is allowed to modify an `OrderLine` directly, and every invariant (an order cannot be empty, `total` is always derived, never assigned) is enforced by a method on `Order` itself. Load and save an aggregate as a whole, never partially.
- **Value Object vs. Entity**: an `Entity` (`Customer`, `Product`) has an identity that persists across changes (a `Customer` is still the same customer after an address update). A `Value Object` (`Money`, `Email`) has no identity - two instances holding the same data are interchangeable - and is immutable: any "change" produces a new instance. Validation lives in the constructor, so an invalid `Money` or `Email` simply cannot exist as an object.
- **Domain Events**: a statement that something meaningful already happened in the domain, named in the past tense (`OrderPlacedEvent`, not `PlaceOrderEvent`). The aggregate raises the event as part of enforcing its own invariants (`Order.place()`); infrastructure is responsible for actually publishing it, typically after the surrounding transaction commits - the domain does not know or care who is listening.
- **Rich domain model vs. anemic domain model**: an anemic model is a set of plain getters/setters with all logic pushed into services (essentially what `spring-boot-tutorial`'s `entity/` package is, by design, since that tutorial's goal was different). A rich model, as used here, pushes business rules into the objects that own the data they constrain, so a class like `Order` cannot be put into an invalid state no matter which code calls it.
- **Ubiquitous Language**: naming in the code should match the vocabulary domain experts actually use (an order is "placed", not "saved with status=1"). This is why `Order.place()` exists as a named method rather than a generic `updateStatus(PLACED)` setter.

## Enriched domain model (DDD)

The flat relational model from the earlier tutorials is deliberately enriched here so the DDD concepts have something to express:

- **`Order`** becomes a real **Aggregate Root**: it holds a collection of **`OrderLine`** (an order can now contain several products, not just one), and enforces its own invariants - an order cannot be created empty, and `total` is never set from the outside, it is **always recomputed by the aggregate itself** from its lines
- **`Money`**: an immutable Value Object (amount + currency) replacing the plain `double unit_price`/`total`, with validation built in (no negative amount) and proper arithmetic (`add`, `multiply`)
- **`Email`**: a Value Object for `Customer`, with format validation built into the constructor
- **`OrderPlacedEvent`**: a Domain Event raised by the `Order` aggregate itself when an order transitions to `PLACED` - published outside the technical transaction by infrastructure, never triggered directly by an application service

`Category`, `Product`, and `Customer` stay conceptually close to the earlier tutorials but are rewritten as plain domain objects (no JPA annotations at all).

## Tech stack

| Component | Choice |
|---|---|
| Framework | Spring Boot 3.5.16 (Spring Framework 6) - only in `bootstrap` and `infrastructure` |
| Language | Java 17 (LTS) |
| Build | Maven, multi-module |
| Database | PostgreSQL 16 (via Docker Compose) |
| ORM | Spring Data JPA / Hibernate - confined to the `infrastructure` module |
| Migrations | Flyway |
| Domain ↔ persistence mapping | MapStruct (or manual mapping) inside `infrastructure` |
| Validation | Business validation carried by the domain itself (constructors, Value Objects), not by Bean Validation annotations on DTOs |
| API documentation | springdoc-openapi (`infrastructure` module) |
| Architecture tests | ArchUnit (`arch-test` module) |
| Tests | JUnit 5, Mockito, Testcontainers |
| CI/CD | GitHub Actions |

## Data model

Starting point, the same EER schema as `spring-boot-tutorial`: `categories` -> `products`, `customers` -> `orders`, one product/quantity per order line.

The schema created by `feature/bootstrap`'s Flyway migration enriches this in two ways: `unit_price` and `total` become a `Money` Value Object (`amount` + `currency`, never a plain float/double), and `orders` gains an `order_lines` table so an order can hold several products instead of one, with `orders.total` always recomputed from its lines rather than stored as an independent input.

```
categories (id, category_name)
    │ 1
    │
    │ N
products (id, category_id, product_name, unit_price_amount, unit_price_currency)
    │ 1
    │
    │ N
order_lines (id, order_id, product_id, quantity, unit_price_amount, unit_price_currency)
    │ N
    │
    │ 1
orders (id, customer_id, status, total_amount, total_currency, placed_at)
    │ N
    │
    │ 1
customers (id, first_name, last_name, telephone, email, address)
```

### Column details

**categories**
| Column | Type | Constraints |
|---|---|---|
| id | BIGINT | PK, auto-increment |
| category_name | VARCHAR(255) | NOT NULL |

**products**
| Column | Type | Constraints |
|---|---|---|
| id | BIGINT | PK, auto-increment |
| category_id | BIGINT | FK -> categories.id, NOT NULL |
| product_name | VARCHAR(255) | NOT NULL |
| unit_price_amount | NUMERIC(19,2) | NOT NULL, >= 0 (`Money.amount`) |
| unit_price_currency | VARCHAR(3) | NOT NULL, ISO 4217 code (`Money.currency`) |

**customers**
| Column | Type | Constraints |
|---|---|---|
| id | BIGINT | PK, auto-increment |
| first_name | VARCHAR(255) | NOT NULL |
| last_name | VARCHAR(255) | NOT NULL |
| telephone | VARCHAR(50) | NOT NULL |
| email | VARCHAR(255) | NOT NULL, UNIQUE, valid email format (`Email` Value Object) |
| address | VARCHAR(255) | NOT NULL |

**orders** (Aggregate Root)
| Column | Type | Constraints |
|---|---|---|
| id | BIGINT | PK, auto-increment |
| customer_id | BIGINT | FK -> customers.id, NOT NULL |
| status | VARCHAR(20) | NOT NULL, `OrderStatus` enum: `DRAFT`, `PLACED`, `CANCELLED` |
| total_amount | NUMERIC(19,2) | NOT NULL, always recomputed by the aggregate from `order_lines`, never set directly |
| total_currency | VARCHAR(3) | NOT NULL |
| placed_at | TIMESTAMP | NULL, set when `Order.place()` transitions the aggregate to `PLACED` |

**order_lines** (owned by `orders`, not an aggregate of its own)
| Column | Type | Constraints |
|---|---|---|
| id | BIGINT | PK, auto-increment |
| order_id | BIGINT | FK -> orders.id, NOT NULL |
| product_id | BIGINT | FK -> products.id, NOT NULL |
| quantity | INT | NOT NULL, > 0 |
| unit_price_amount | NUMERIC(19,2) | NOT NULL, `Money` captured at line creation time, not re-read from `products` later |
| unit_price_currency | VARCHAR(3) | NOT NULL |

## Module dependency rules

```
        ┌─────────────┐
        │  bootstrap   │   (knows everything: application, infrastructure, domain)
        └──────┬───────┘
               │ depends on
     ┌─────────┴──────────┐
     │                    │
┌────▼─────┐      ┌───────▼────────┐
│ application │◄────│ infrastructure │   (infrastructure implements domain's ports)
└────┬───────┘      └────────────────┘
     │ depends on
┌────▼─────┐
│  domain   │   (depends on NOTHING - declares port/in and port/out)
└───────────┘

arch-test → depends on domain, application, infrastructure, bootstrap (test scope)
            and verifies the arrows above are never reversed
```

- `domain`: **no dependency** on any other module in the project, nor on Spring/JPA - declares `port/in` and `port/out` (Eric Evans' Repository pattern, extended to every port)
- `application`: depends only on `domain`; **implements** `domain`'s `port/in` interfaces and **calls** its `port/out` interfaces - it declares no port of its own
- `infrastructure`: depends on `application` (to call the use cases it implements) and directly on `domain` (to implement its `port/out` interfaces)
- `bootstrap`: depends on `application` and `infrastructure` (the only module with `spring-boot-maven-plugin` and an executable `jar` packaging)
- `arch-test`: depends on every module (`test` scope), contains only architecture tests, never production code

## Module structure

```
spring-hexagonal-ddd-tutorial/
├── pom.xml                                  (parent, packaging=pom, dependencyManagement, module list)
│
├── domain/                                  (jar - zero Spring/JPA dependency)
│   └── src/main/java/com/edgareldy/domain/
│       ├── model/
│       │   ├── shared/
│       │   │   ├── Money.java               (Value Object)
│       │   │   └── Email.java               (Value Object)
│       │   ├── category/
│       │   │   └── Category.java
│       │   ├── product/
│       │   │   └── Product.java
│       │   ├── customer/
│       │   │   └── Customer.java
│       │   └── order/
│       │       ├── Order.java                (Aggregate Root)
│       │       ├── OrderLine.java
│       │       └── OrderStatus.java          (enum: DRAFT, PLACED, CANCELLED)
│       ├── port/                              (Evans' Repository pattern, extended to every port)
│       │   ├── in/
│       │   │   ├── CreateOrderUseCase.java
│       │   │   ├── GetOrderUseCase.java
│       │   │   ├── ListOrdersUseCase.java
│       │   │   ├── CreateProductUseCase.java
│       │   │   └── command/
│       │   │       └── CreateOrderCommand.java   (record, input of the use case)
│       │   └── out/
│       │       ├── OrderRepositoryPort.java
│       │       ├── ProductRepositoryPort.java
│       │       ├── CustomerRepositoryPort.java
│       │       ├── CategoryRepositoryPort.java
│       │       └── DomainEventPublisherPort.java
│       ├── event/
│       │   ├── DomainEvent.java              (marker interface)
│       │   └── OrderPlacedEvent.java
│       └── exception/
│           ├── DomainException.java
│           ├── EmptyOrderException.java
│           └── InsufficientStockException.java
│
├── application/                             (jar - depends only on domain, declares no port of its own)
│   └── src/main/java/com/edgareldy/application/
│       └── service/
│           ├── CreateOrderService.java        (implements domain's CreateOrderUseCase)
│           ├── GetOrderService.java
│           ├── ListOrdersService.java
│           └── CreateProductService.java
│
├── infrastructure/                          (jar - depends on application + domain)
│   └── src/main/java/com/edgareldy/infrastructure/
│       ├── in/web/
│       │   ├── controller/
│       │   │   ├── OrderController.java
│       │   │   ├── ProductController.java
│       │   │   ├── CustomerController.java
│       │   │   └── CategoryController.java
│       │   ├── dto/
│       │   │   ├── common/ (ApiResponse.java, PageResponse.java)
│       │   │   ├── order/ (OrderRequest, OrderLineRequest, OrderResponse)
│       │   │   ├── product/ (ProductRequest, ProductResponse)
│       │   │   └── customer/ (CustomerRequest, CustomerResponse)
│       │   ├── mapper/ (web DTO ↔ domain objects / commands)
│       │   └── exception/
│       │       └── GlobalExceptionHandler.java   (translates DomainException into HTTP responses)
│       └── out/
│           ├── persistence/
│           │   ├── entity/                   (JPA entities - DISTINCT from domain objects)
│           │   │   ├── OrderEntity.java
│           │   │   ├── OrderLineEntity.java
│           │   │   ├── ProductEntity.java
│           │   │   ├── CategoryEntity.java
│           │   │   └── CustomerEntity.java
│           │   ├── repository/                (Spring Data JPA, on the *Entity classes)
│           │   │   ├── OrderJpaRepository.java
│           │   │   ├── ProductJpaRepository.java
│           │   │   ├── CategoryJpaRepository.java
│           │   │   └── CustomerJpaRepository.java
│           │   ├── mapper/                     (domain ↔ JPA entity)
│           │   │   ├── OrderPersistenceMapper.java
│           │   │   └── ProductPersistenceMapper.java
│           │   └── adapter/                    (implement domain's outbound ports)
│           │       ├── OrderRepositoryAdapter.java
│           │       ├── ProductRepositoryAdapter.java
│           │       ├── CategoryRepositoryAdapter.java
│           │       └── CustomerRepositoryAdapter.java
│           └── event/
│               └── SpringDomainEventPublisherAdapter.java   (implements DomainEventPublisherPort via ApplicationEventPublisher)
│
├── bootstrap/                                (executable jar - knows everything, the only module with @SpringBootApplication)
│   └── src/main/
│       ├── java/com/edgareldy/bootstrap/
│       │   ├── HexagonalDddTutorialApplication.java   (@SpringBootApplication, scanBasePackages across all modules)
│       │   └── config/
│       │       └── OpenApiConfig.java
│       └── resources/
│           ├── application.yml
│           ├── application-dev.yml
│           ├── application-test.yml
│           └── db/migration/
│               └── V1__init_schema.sql
│
└── arch-test/                                 (jar - ArchUnit tests only, depends on every module)
    └── src/test/java/com/edgareldy/archtest/
        ├── DomainIndependenceTest.java         (domain depends on nothing else)
        ├── ApplicationDependencyTest.java      (application does not depend on infrastructure)
        ├── LayeredArchitectureTest.java        (global rule via ArchUnit onionArchitecture()/layeredArchitecture())
        └── NamingConventionTest.java           (port.in ends with UseCase, port.out with Port, adapters with Adapter)
```

## Standard response format

Every API response (success and error alike) is wrapped in a generic `ApiResponse<T>` DTO, defined in `infrastructure/.../in/web/dto/common/ApiResponse.java`, to keep a consistent contract across all endpoints. Domain objects and Commands never appear directly in this envelope: `data` always holds a Response DTO, mapped from the domain object by an `infrastructure` mapper.

```java
public record ApiResponse<T>(
        boolean success,
        String message,
        T data,
        Instant timestamp
) {
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(true, message, data, Instant.now());
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null, Instant.now());
    }
}
```

- On list endpoints, `data` holds a `PageResponse<T>` (paginated content: `content`, `page`, `size`, `totalElements`, `totalPages`) instead of a plain `List<T>`.
- On error, `GlobalExceptionHandler` returns an `ApiResponse<Void>` with `success=false` and an explicit `message`, translating each `DomainException` into the matching HTTP status.
- `Money` and other Value Objects are serialized as nested JSON objects, not flattened scalars, so `amount` and `currency` always travel together.
- Success response example (`POST /api/v1/orders`):

```json
{
  "success": true,
  "message": "Order placed successfully",
  "data": {
    "id": 42,
    "customerId": 7,
    "status": "PLACED",
    "lines": [
      {
        "productId": 12,
        "productName": "Mechanical keyboard",
        "quantity": 2,
        "unitPrice": { "amount": 79.99, "currency": "EUR" },
        "subtotal": { "amount": 159.98, "currency": "EUR" }
      }
    ],
    "total": { "amount": 159.98, "currency": "EUR" }
  },
  "timestamp": "2026-07-11T22:30:00Z"
}
```

- Error response example (`EmptyOrderException` -> 422):

```json
{
  "success": false,
  "message": "An order cannot be placed without at least one line",
  "data": null,
  "timestamp": "2026-07-11T22:30:00Z"
}
```

## Branching strategy

| Branch | Role |
|---|---|
| `master` | Stable code. No direct commits, only merges from `develop`. |
| `develop` | Integration branch. |
| `feature/domain` | `domain` module: Aggregates, Value Objects, Domain Events, business exceptions. |
| `feature/application` | `application` module: inbound/outbound ports, use cases. |
| `feature/infrastructure` | `infrastructure` module: REST and JPA adapters. |
| `feature/bootstrap` | `bootstrap` module: runnable Spring Boot application, configuration, migrations. |
| `feature/arch-test` | `arch-test` module: ArchUnit tests validating the dependency rules. |

## feature/domain

No external dependency beyond the JDK. No Spring, JPA, or Bean Validation annotation.

### Tasks

- [x] `Money` (Value Object): `amount`/`currency` fields, immutable, validation in the constructor (amount ≥ 0), `add`, `multiply` methods
- [x] `Email` (Value Object): format validation in the constructor
- [x] `Category`, `Product` (with `unitPrice: Money`), `Customer` (with `email: Email`)
- [x] `OrderLine` (product + quantity + computed subtotal)
- [x] `Order` (Aggregate Root): collection of `OrderLine`, a `place()` method that validates invariants (order not empty) and raises `OrderPlacedEvent`, `total` recomputed internally, never assignable from the outside
- [x] `OrderStatus` (enum), `DomainEvent`/`OrderPlacedEvent`
- [x] Business exceptions (`EmptyOrderException`, `InsufficientStockException`)
- [x] `port/in` interfaces (Evans' Repository pattern extended to every port): one use case per business action (`CreateOrderUseCase`, `GetOrderUseCase`, `ListOrdersUseCase`, `CreateProductUseCase`, etc.), each with a single method and a `Command`/`Query` input (record) in `port/in/command/`
- [x] `port/out` interfaces: `OrderRepositoryPort`, `ProductRepositoryPort`, `CustomerRepositoryPort`, `CategoryRepositoryPort`, `DomainEventPublisherPort` - operating only on domain objects, never on DTOs or JPA entities
- [x] Pure unit tests (JUnit 5 only, no Spring context) on the aggregate's invariants and the Value Objects

## feature/application

Depends only on `domain`. Declares no port of its own - only implements `domain`'s `port/in` and calls its `port/out`.

### Tasks

- [x] Implementations in `service/`, one per `domain.port.in` use case (e.g. `CreateOrderService implements CreateOrderUseCase`): orchestrate the domain (load/create the aggregate, call its business methods) and call the domain's outbound ports (persistence, event publishing) - **no business rule lives here**, orchestration only
- [x] Tests with Mockito on the outbound ports (no Spring, no database)

## feature/infrastructure

Depends on `application` and `domain`.

### Endpoints

| Method | URL | Description |
|---|---|---|
| GET | `/api/v1/categories` | Paginated list |
| POST | `/api/v1/categories` | Create |
| GET | `/api/v1/products` | Paginated list, filterable by `categoryId` |
| POST | `/api/v1/products` | Create |
| GET | `/api/v1/customers/{id}` | Detail |
| POST | `/api/v1/customers` | Create |
| POST | `/api/v1/orders` | Create an order (multiple lines) |
| GET | `/api/v1/orders/{id}` | Detail |
| GET | `/api/v1/orders` | Paginated list |

### Tasks

- [x] JPA entities (`*Entity`), distinct from domain objects, with `jakarta.persistence.*` annotations
- [x] Spring Data JPA repositories on the `*Entity` classes
- [x] Persistence mappers (domain ↔ JPA entity)
- [x] Adapters (`*RepositoryAdapter`) implementing `domain`'s outbound ports, using the JPA repositories + mappers
- [x] `SpringDomainEventPublisherAdapter` implementing `DomainEventPublisherPort` via `ApplicationEventPublisher`
- [x] REST controllers (`/api/v1/...`): inject **use cases** (`port/in`), never repositories or adapters directly
- [x] Request/Response DTOs, DTO ↔ Command/domain mappers, generic `ApiResponse<T>`
- [x] `GlobalExceptionHandler`: translates every `DomainException` into the appropriate HTTP status (e.g. `EmptyOrderException` → 422)
- [x] `@WebMvcTest` tests (controllers, use cases mocked) and `@DataJpaTest` tests (persistence adapters)

## feature/bootstrap

Depends on `application` and `infrastructure`. The only module packaged as an executable jar.

### Tasks

- [ ] `HexagonalDddTutorialApplication` (`@SpringBootApplication`), with `scanBasePackages`/`@EntityScan`/`@EnableJpaRepositories` explicitly pointing at the `infrastructure` packages (the beans do not live in the same module as the main class)
- [ ] `application.yml`/`application-dev.yml`/`application-test.yml`: datasource, ports, Actuator configuration
- [ ] Flyway script `V1__init_schema.sql` (schema including the `order_lines` table)
- [ ] `OpenApiConfig`
- [ ] `docker-compose.yml` + `Dockerfile`
- [ ] `.github/workflows/ci.yml`: multi-module build (`mvn -T 1C clean verify`)
- [ ] End-to-end integration test (`@SpringBootTest`, Testcontainers) verifying the full chain controller → use case → domain → adapter → database

## feature/arch-test

Depends on every module, `test` scope only. Contains no production code.

### Tasks

- [x] `com.tngtech.archunit:archunit-junit5` dependency
- [x] `DomainIndependenceTest`: verifies `com.edgareldy.domain..` does not depend on any class from `application`, `infrastructure`, `bootstrap`, nor on `org.springframework..`/`jakarta.persistence..` packages
- [x] `ApplicationDependencyTest`: verifies `application..` never depends on `infrastructure..`, and never declares its own `port.in`/`port.out` package (ports live in `domain` only)
- [x] `LayeredArchitectureTest`: global rule via `ArchRuleDefinition.layeredArchitecture()` declaring the 4 layers (domain, application, infrastructure, bootstrap) and the allowed access between them
- [x] `NamingConventionTest`: interfaces in `domain.port.in` end in `UseCase`, those in `domain.port.out` end in `Port`, adapters in `infrastructure.out` end in `Adapter`
- [ ] These tests run in CI on every Pull Request: any architectural violation fails the build, independently of functional tests (the GitHub Actions workflow itself is a `feature/bootstrap` task, not yet written)

## Order of work

1. `feature/domain` → Pull Request to `develop`
2. `feature/application` (depends on `domain`) → Pull Request to `develop`
3. `feature/infrastructure` (depends on `application`) → Pull Request to `develop`
4. `feature/bootstrap` (depends on `application` and `infrastructure`) → Pull Request to `develop`
5. `feature/arch-test` (depends on every module, can also be started as early as `feature/domain` and enriched along the way) → Pull Request to `develop`
6. `develop` → `master`

## Code conventions

- Root package: `com.edgareldy`, one sub-package per module (`.domain`, `.application`, `.infrastructure`, `.bootstrap`, `.archtest`)
- The `domain` module **never** imports `org.springframework.*` or `jakarta.persistence.*`, including in its `port` package - this rule is mechanically checked by `feature/arch-test`, not just documented
- Domain objects are **never** exposed directly over HTTP (DTOs live in `infrastructure`) nor persisted directly (separate JPA entities in `infrastructure`) - always an explicit mapping
- Ports are declared in `domain` (Eric Evans' Repository pattern, extended to every port), never in `application`:
  - An inbound port (use case) = an interface in `domain.port.in` with a single business method, named `<Action><Resource>UseCase` (e.g. `CreateOrderUseCase`); `application` provides its implementation (e.g. `CreateOrderService`)
  - An outbound port = an interface in `domain.port.out` named `<Resource>Port` (e.g. `OrderRepositoryPort`), operating only on domain types
- Every business rule (invariants, calculations) lives in the domain, never in application services or adapters
- Every controller returns an `ApiResponse<T>` (`infrastructure` module only)

## Concepts covered

- Hexagonal architecture (ports & adapters)
- Domain-Driven Design: Aggregate Root, Value Objects, Domain Events, business invariants owned by the domain
- Strict separation between the domain model and the persistence model (distinct JPA entities)
- Explicit use cases (one inbound port per business action)
- Automated architecture tests (ArchUnit) wired into CI
- Multi-module Maven with strict dependency rules between layers
- Spring Data JPA confined to the infrastructure layer
- Generic `ApiResponse<T>` DTO (infrastructure side only)
- Tests per layer: pure domain tests (no Spring), application tests with mocks, infrastructure tests (`@WebMvcTest`, `@DataJpaTest`), end-to-end integration test
- Continuous integration on a multi-module Maven build with architectural verification

## How to follow this tutorial

1. Clone the repository and check out `develop`
2. Follow the modules in order: `feature/domain` → `feature/application` → `feature/infrastructure` → `feature/bootstrap`
3. Start `feature/arch-test` in parallel as soon as `feature/domain` exists, and enrich it with each new module
4. Build everything from the root: `mvn clean verify` (ArchUnit tests run alongside regular tests)
5. Run `bootstrap/target/hexagonal-ddd-tutorial.jar` (or `mvn spring-boot:run -pl bootstrap`), then open Swagger UI at `http://localhost:8080/swagger-ui.html`

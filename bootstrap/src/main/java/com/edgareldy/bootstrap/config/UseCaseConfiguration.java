package com.edgareldy.bootstrap.config;

import com.edgareldy.application.service.CreateCategoryService;
import com.edgareldy.application.service.CreateCustomerService;
import com.edgareldy.application.service.CreateOrderService;
import com.edgareldy.application.service.CreateProductService;
import com.edgareldy.application.service.GetCustomerService;
import com.edgareldy.application.service.GetOrderService;
import com.edgareldy.application.service.ListCategoriesService;
import com.edgareldy.application.service.ListOrdersService;
import com.edgareldy.application.service.ListProductsService;
import com.edgareldy.domain.port.in.CreateCategoryUseCase;
import com.edgareldy.domain.port.in.CreateCustomerUseCase;
import com.edgareldy.domain.port.in.CreateOrderUseCase;
import com.edgareldy.domain.port.in.CreateProductUseCase;
import com.edgareldy.domain.port.in.GetCustomerUseCase;
import com.edgareldy.domain.port.in.GetOrderUseCase;
import com.edgareldy.domain.port.in.ListCategoriesUseCase;
import com.edgareldy.domain.port.in.ListOrdersUseCase;
import com.edgareldy.domain.port.in.ListProductsUseCase;
import com.edgareldy.domain.port.out.CategoryRepositoryPort;
import com.edgareldy.domain.port.out.CustomerRepositoryPort;
import com.edgareldy.domain.port.out.DomainEventPublisherPort;
import com.edgareldy.domain.port.out.OrderRepositoryPort;
import com.edgareldy.domain.port.out.ProductRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Explicit {@code @Bean} wiring for every {@code application} service: {@code application}
 * carries no Spring annotation of its own (no {@code @Service}/{@code @Component}), so nothing
 * here is found by classpath scanning, and each use case must be constructed by hand from the
 * outbound port beans {@code infrastructure} provides.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
@Configuration
public class UseCaseConfiguration {

    @Bean
    public CreateCategoryUseCase createCategoryUseCase(CategoryRepositoryPort categoryRepositoryPort) {
        return new CreateCategoryService(categoryRepositoryPort);
    }

    @Bean
    public ListCategoriesUseCase listCategoriesUseCase(CategoryRepositoryPort categoryRepositoryPort) {
        return new ListCategoriesService(categoryRepositoryPort);
    }

    @Bean
    public CreateProductUseCase createProductUseCase(ProductRepositoryPort productRepositoryPort,
            CategoryRepositoryPort categoryRepositoryPort) {
        return new CreateProductService(productRepositoryPort, categoryRepositoryPort);
    }

    @Bean
    public ListProductsUseCase listProductsUseCase(ProductRepositoryPort productRepositoryPort) {
        return new ListProductsService(productRepositoryPort);
    }

    @Bean
    public CreateCustomerUseCase createCustomerUseCase(CustomerRepositoryPort customerRepositoryPort) {
        return new CreateCustomerService(customerRepositoryPort);
    }

    @Bean
    public GetCustomerUseCase getCustomerUseCase(CustomerRepositoryPort customerRepositoryPort) {
        return new GetCustomerService(customerRepositoryPort);
    }

    @Bean
    public GetOrderUseCase getOrderUseCase(OrderRepositoryPort orderRepositoryPort) {
        return new GetOrderService(orderRepositoryPort);
    }

    @Bean
    public ListOrdersUseCase listOrdersUseCase(OrderRepositoryPort orderRepositoryPort) {
        return new ListOrdersService(orderRepositoryPort);
    }

    /**
     * The only use case wrapped in a transactional decorator: see
     * {@link TransactionalCreateOrderUseCase} for why {@code CreateOrderService} specifically
     * needs one.
     */
    @Bean
    public CreateOrderUseCase createOrderUseCase(OrderRepositoryPort orderRepositoryPort,
            ProductRepositoryPort productRepositoryPort, CustomerRepositoryPort customerRepositoryPort,
            DomainEventPublisherPort domainEventPublisherPort) {
        CreateOrderService delegate = new CreateOrderService(orderRepositoryPort, productRepositoryPort,
                customerRepositoryPort, domainEventPublisherPort);
        return new TransactionalCreateOrderUseCase(delegate);
    }
}

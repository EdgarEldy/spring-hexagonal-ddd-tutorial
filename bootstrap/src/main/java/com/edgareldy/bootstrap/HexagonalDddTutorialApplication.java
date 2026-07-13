package com.edgareldy.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * The only module with an executable entry point: {@code application}'s services and
 * {@code infrastructure}'s controllers/adapters/entities do not live in this module's own
 * package, so component scanning, entity scanning, and Spring Data repository scanning all have
 * to be pointed at {@code com.edgareldy.infrastructure} explicitly, otherwise Spring Boot's
 * default single-package scan would silently find none of them.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
@SpringBootApplication(scanBasePackages = {"com.edgareldy.bootstrap", "com.edgareldy.infrastructure"})
@EntityScan("com.edgareldy.infrastructure.out.persistence.entity")
@EnableJpaRepositories("com.edgareldy.infrastructure.out.persistence.repository")
public class HexagonalDddTutorialApplication {

    public static void main(String[] args) {
        SpringApplication.run(HexagonalDddTutorialApplication.class, args);
    }
}

package com.edgareldy.infrastructure;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

/**
 * Minimal {@code @SpringBootConfiguration} so that slice tests ({@code @DataJpaTest},
 * {@code @WebMvcTest}) in this module can bootstrap: {@code infrastructure} has no
 * {@code @SpringBootApplication} class of its own, that lives in {@code bootstrap}, and Spring
 * Test's context bootstrapper needs one findable by searching upward from the test's package.
 * Deliberately no {@code @ComponentScan}: {@code @DataJpaTest} finds Spring Data repositories
 * via {@code AutoConfigurationPackages} regardless of scanning, and a module-wide scan would
 * make every {@code @WebMvcTest} pull in all four controllers instead of just the one under
 * test, since {@code @WebMvcTest(controllers = ...)} only narrows an existing scan rather than
 * excluding from it reliably in that configuration. Each {@code @WebMvcTest} instead
 * {@code @Import}s exactly the controller (and {@code GlobalExceptionHandler}) it needs.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
@SpringBootConfiguration
@EnableAutoConfiguration
class InfrastructureTestApplication {
}

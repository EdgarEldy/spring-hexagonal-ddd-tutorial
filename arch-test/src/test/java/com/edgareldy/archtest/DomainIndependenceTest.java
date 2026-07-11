package com.edgareldy.archtest;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * Verifies mechanically what the whole project's architecture rests on: {@code domain} depends
 * on nothing else, not on the other modules and not on Spring/JPA, even in its {@code port}
 * package. Without this test, that guarantee would only ever be checked by manual review.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
@AnalyzeClasses(packages = "com.edgareldy")
class DomainIndependenceTest {

    @ArchTest
    static final ArchRule domain_does_not_depend_on_other_modules = noClasses()
            .that().resideInAPackage("com.edgareldy.domain..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("com.edgareldy.application..", "com.edgareldy.infrastructure..",
                    "com.edgareldy.bootstrap..")
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule domain_does_not_depend_on_spring = noClasses()
            .that().resideInAPackage("com.edgareldy.domain..")
            .should().dependOnClassesThat().resideInAnyPackage("org.springframework..")
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule domain_does_not_depend_on_jpa = noClasses()
            .that().resideInAPackage("com.edgareldy.domain..")
            .should().dependOnClassesThat().resideInAnyPackage("jakarta.persistence..")
            .allowEmptyShould(true);
}

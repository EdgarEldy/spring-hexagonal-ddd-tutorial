package com.edgareldy.archtest;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * Verifies that {@code application} stays a thin orchestration layer: it never reaches into
 * {@code infrastructure}/{@code bootstrap}, and it never declares a port of its own, since
 * ports (Eric Evans' Repository pattern, extended to every port) belong to {@code domain} only.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
@AnalyzeClasses(packages = "com.edgareldy")
class ApplicationDependencyTest {

    @ArchTest
    static final ArchRule application_does_not_depend_on_infrastructure_or_bootstrap = noClasses()
            .that().resideInAPackage("com.edgareldy.application..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("com.edgareldy.infrastructure..", "com.edgareldy.bootstrap..")
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule application_declares_no_port_package = noClasses()
            .that().resideInAPackage("com.edgareldy.application..")
            .should().resideInAPackage("..port..")
            .allowEmptyShould(true);
}

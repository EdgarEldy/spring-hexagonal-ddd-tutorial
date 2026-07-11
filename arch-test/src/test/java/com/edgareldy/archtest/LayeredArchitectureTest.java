package com.edgareldy.archtest;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

/**
 * The global rule, in one place: declares the four layers and the only access directions the
 * README allows between them, complementing {@link DomainIndependenceTest} and
 * {@link ApplicationDependencyTest}'s narrower, single-module rules with one architecture-wide
 * check.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
@AnalyzeClasses(packages = "com.edgareldy")
class LayeredArchitectureTest {

    // withOptionalLayers(true): infrastructure has no classes yet as of feature/arch-test, and
    // layeredArchitecture() otherwise fails on an empty layer, which would make this rule
    // impossible to add before feature/infrastructure exists.
    @ArchTest
    static final ArchRule layer_dependencies_are_respected = layeredArchitecture()
            .consideringAllDependencies()
            .withOptionalLayers(true)
            .layer("Domain").definedBy("com.edgareldy.domain..")
            .layer("Application").definedBy("com.edgareldy.application..")
            .layer("Infrastructure").definedBy("com.edgareldy.infrastructure..")
            .layer("Bootstrap").definedBy("com.edgareldy.bootstrap..")
            .whereLayer("Domain").mayOnlyBeAccessedByLayers("Application", "Infrastructure", "Bootstrap")
            .whereLayer("Application").mayOnlyBeAccessedByLayers("Infrastructure", "Bootstrap")
            .whereLayer("Infrastructure").mayOnlyBeAccessedByLayers("Bootstrap")
            .whereLayer("Bootstrap").mayNotBeAccessedByAnyLayer();
}

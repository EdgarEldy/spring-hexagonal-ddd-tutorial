package com.edgareldy.archtest;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

/**
 * Locks in the naming convention the README relies on to make the ports/adapters pattern
 * legible: an inbound port ends in {@code UseCase}, an outbound port ends in {@code Port}, and
 * an adapter implementing an outbound port ends in {@code Adapter}.
 * <p>
 * Created by edgar.muhamyangabo on 7/12/26
 * Author : edgar.muhamyangabo
 * Date : 7/12/26
 * Project : spring-hexagonal-ddd-tutorial
 */
@AnalyzeClasses(packages = "com.edgareldy")
class NamingConventionTest {

    // getAllRawInterfaces() (transitive), not getRawInterfaces() (direct only): an adapter
    // implementing a port through an intermediate abstract base class must still be caught.
    // The exact package equality check, not startsWith, avoids matching an unrelated sibling
    // package such as a hypothetical com.edgareldy.domain.port.output.
    private static final DescribedPredicate<JavaClass> IMPLEMENTS_AN_OUTBOUND_PORT = DescribedPredicate.describe(
            "implement a domain.port.out interface",
            javaClass -> javaClass.getAllRawInterfaces().stream()
                    .anyMatch(rawInterface -> rawInterface.getPackageName().equals("com.edgareldy.domain.port.out")));

    @ArchTest
    static final ArchRule inbound_port_interfaces_end_with_use_case = classes()
            .that().resideInAPackage("com.edgareldy.domain.port.in")
            .and().areInterfaces()
            .should().haveSimpleNameEndingWith("UseCase");

    @ArchTest
    static final ArchRule outbound_port_interfaces_end_with_port = classes()
            .that().resideInAPackage("com.edgareldy.domain.port.out")
            .and().areInterfaces()
            .should().haveSimpleNameEndingWith("Port");

    @ArchTest
    static final ArchRule adapters_implementing_an_outbound_port_end_with_adapter = classes()
            .that().resideInAPackage("com.edgareldy.infrastructure.out..")
            .and(IMPLEMENTS_AN_OUTBOUND_PORT)
            .should().haveSimpleNameEndingWith("Adapter")
            .allowEmptyShould(true);
}

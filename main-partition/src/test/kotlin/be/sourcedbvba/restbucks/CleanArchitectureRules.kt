package be.sourcedbvba.restbucks

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition

fun CleanArchitectureDefinition.check(classes: JavaClasses) {
    val rules: List<ArchRule> = listOf(
        applicationApiRules(this),
        applicationImplRules(this),
        domainRules(this),
        consumingInfraRules(this),
        implementingInfraRules(this)
    )
    rules.forEach { it.check(classes) }
}

fun applicationApiRules(definition: CleanArchitectureDefinition): ArchRule {
    return ArchRuleDefinition.noClasses()
            .that().resideInAPackage("${definition.boundedContextPackage}.${definition.applicationBoundarySubPackage}..")
            .should().accessClassesThat().resideInAnyPackage(
                    "${definition.boundedContextPackage}.${definition.applicationInteractorSubPackage}..",
                    "${definition.boundedContextPackage}.${definition.domainModelSubPackage}..",
                    "${definition.boundedContextPackage}.${definition.domainServicesSubPackage}..",
                    "${definition.boundedContextPackage}.${definition.implementingInfrastructureSubPackage}..",
                    "${definition.boundedContextPackage}.${definition.consumingInfrastructureSubPackage}..",
                    "${definition.boundedContextPackage}.${definition.mainPartitionSubPackage}..")
}


fun applicationImplRules(definition: CleanArchitectureDefinition): ArchRule {
    return ArchRuleDefinition.noClasses()
            .that().resideInAPackage("${definition.boundedContextPackage}.${definition.applicationInteractorSubPackage}..")
            .should().accessClassesThat().resideInAnyPackage(
                    "${definition.boundedContextPackage}.${definition.implementingInfrastructureSubPackage}..",
                    "${definition.boundedContextPackage}.${definition.consumingInfrastructureSubPackage}..",
                    "${definition.boundedContextPackage}.${definition.mainPartitionSubPackage}..")
}

fun domainRules(definition: CleanArchitectureDefinition): ArchRule {
    return ArchRuleDefinition.noClasses()
            .that().resideInAnyPackage("${definition.boundedContextPackage}.${definition.domainModelSubPackage}..",
                    "${definition.boundedContextPackage}.${definition.domainServicesSubPackage}..")
            .should().accessClassesThat().resideInAnyPackage(
                    "${definition.boundedContextPackage}.${definition.applicationBoundarySubPackage}..",
                    "${definition.boundedContextPackage}.${definition.applicationInteractorSubPackage}..",
                    "${definition.boundedContextPackage}.${definition.implementingInfrastructureSubPackage}..",
                    "${definition.boundedContextPackage}.${definition.consumingInfrastructureSubPackage}..",
                    "${definition.boundedContextPackage}.${definition.mainPartitionSubPackage}..")
}

fun consumingInfraRules(definition: CleanArchitectureDefinition): ArchRule {
    return ArchRuleDefinition.noClasses()
            .that().resideInAPackage("${definition.boundedContextPackage}.${definition.consumingInfrastructureSubPackage}..")
            .should().accessClassesThat().resideInAnyPackage(
                    "${definition.boundedContextPackage}.${definition.domainModelSubPackage}..",
                    "${definition.boundedContextPackage}.${definition.domainServicesSubPackage}..",
                    "${definition.boundedContextPackage}.${definition.applicationInteractorSubPackage}..",
                    "${definition.boundedContextPackage}.${definition.implementingInfrastructureSubPackage}..",
                    "${definition.boundedContextPackage}.${definition.mainPartitionSubPackage}..")
}


fun implementingInfraRules(definition: CleanArchitectureDefinition): ArchRule {
    return ArchRuleDefinition.noClasses()
            .that().resideInAPackage("${definition.boundedContextPackage}.${definition.implementingInfrastructureSubPackage}..")
            .should().accessClassesThat().resideInAnyPackage(
                    "${definition.boundedContextPackage}.${definition.applicationBoundarySubPackage}..",
                    "${definition.boundedContextPackage}.${definition.applicationInteractorSubPackage}..",
                    "${definition.boundedContextPackage}.${definition.consumingInfrastructureSubPackage}..",
                    "${definition.boundedContextPackage}.${definition.mainPartitionSubPackage}..")
}

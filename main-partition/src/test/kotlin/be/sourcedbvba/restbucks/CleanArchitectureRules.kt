package be.sourcedbvba.restbucks

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition

fun CleanArchitectureDefinition.check(classes: JavaClasses) {
    this.boundedContexts.forEach { bc ->
        val rules: List<ArchRule> = listOf(
                *applicationApiRules(bc),
                *applicationImplRules(bc),
                *domainRules(bc),
                *consumingInfraRules(bc),
                *implementingInfraRules(bc),
                *sharedVocabularyRules(bc)
        )
        rules.forEach { it.check(classes) }
    }
}


fun applicationApiRules(definition: BoundedContextDefinition): Array<ArchRule> {
    return definition.applicationBoundaryPackages.map {
        ArchRuleDefinition.noClasses()
                .that().resideInAPackage(it)
                .should().accessClassesThat().resideInAnyPackage(
                        *definition.applicationInteractorPackages,
                        *definition.domainModelPackages,
                        *definition.domainServicesPackages,
                        *definition.implementingInfrastructurePackages,
                        *definition.consumingInfrastructurePackages,
                        *definition.mainPartitionPackages)
    }.toTypedArray()
}


fun applicationImplRules(definition: BoundedContextDefinition): Array<ArchRule> {
    return definition.applicationInteractorPackages.map {
        ArchRuleDefinition.noClasses()
                .that().resideInAPackage(it)
                .should().accessClassesThat().resideInAnyPackage(
                        *definition.implementingInfrastructurePackages,
                        *definition.consumingInfrastructurePackages,
                        *definition.mainPartitionPackages)
    }.toTypedArray()
}

fun domainRules(definition: BoundedContextDefinition): Array<ArchRule> {
    return arrayOf(
            *definition.domainModelPackages.map {
                ArchRuleDefinition.noClasses()
                        .that().resideInAPackage(it)
                        .should().accessClassesThat().resideInAnyPackage(
                                *definition.applicationBoundaryPackages,
                                *definition.applicationInteractorPackages,
                                *definition.implementingInfrastructurePackages,
                                *definition.consumingInfrastructurePackages,
                                *definition.mainPartitionPackages)
            }.toTypedArray(),
            *definition.domainServicesPackages.map {
                ArchRuleDefinition.noClasses()
                        .that().resideInAPackage(it)
                        .should().accessClassesThat().resideInAnyPackage(
                                *definition.applicationBoundaryPackages,
                                *definition.applicationInteractorPackages,
                                *definition.implementingInfrastructurePackages,
                                *definition.consumingInfrastructurePackages,
                                *definition.mainPartitionPackages)
            }.toTypedArray()
    )
}

fun consumingInfraRules(definition: BoundedContextDefinition): Array<ArchRule> {
    return definition.consumingInfrastructurePackages.map {
        ArchRuleDefinition.noClasses()
                .that().resideInAPackage(it)
                .should().accessClassesThat().resideInAnyPackage(
                        *definition.implementingInfrastructurePackages,
                        *definition.domainServicesPackages,
                        *definition.domainModelPackages,
                        *definition.applicationInteractorPackages,
                        *definition.mainPartitionPackages)
    }.toTypedArray()
}


fun implementingInfraRules(definition: BoundedContextDefinition): Array<ArchRule> {
    return definition.implementingInfrastructurePackages.map {
        ArchRuleDefinition.noClasses()
                .that().resideInAPackage(it)
                .should().accessClassesThat().resideInAnyPackage(
                        *definition.consumingInfrastructurePackages,
                        *definition.applicationBoundaryPackages,
                        *definition.applicationInteractorPackages,
                        *definition.mainPartitionPackages)
    }.toTypedArray()
}

fun sharedVocabularyRules(definition: BoundedContextDefinition): Array<ArchRule> {
    return definition.sharedVocabularyPackages.map {
        ArchRuleDefinition.noClasses()
                .that().resideInAPackage(it)
                .should().accessClassesThat().resideInAnyPackage(
                        *definition.implementingInfrastructurePackages,
                        *definition.consumingInfrastructurePackages,
                        *definition.applicationBoundaryPackages,
                        *definition.domainServicesPackages,
                        *definition.domainModelPackages,
                        *definition.applicationInteractorPackages,
                        *definition.mainPartitionPackages)
    }.toTypedArray()
}

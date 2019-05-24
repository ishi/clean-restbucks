package be.sourcedbvba.restbucks.order

import com.tngtech.archunit.base.PackageMatchers
import com.tngtech.archunit.core.domain.JavaClass
import com.tngtech.archunit.lang.ArchCondition
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.CompositeArchRule
import com.tngtech.archunit.lang.ConditionEvents
import com.tngtech.archunit.lang.SimpleConditionEvent
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition

fun CleanArchitectureDefinition.rules(): List<ArchRule> {
    return this.boundedContexts.flatMap { bc ->
        listOf(
                applicationApiRules(bc),
                applicationImplRules(bc),
                domainRules(bc),
                consumingInfraRules(bc),
                implementingInfraRules(bc),
                sharedVocabularyRules(bc),
                mainPartitionRules(bc)
        ).flatten()
    }
}


fun applicationApiRules(definition: BoundedContextDefinition): List<ArchRule> {
    val allowedPackages = arrayOf(
            *definition.whiteListPackages,
            *definition.applicationBoundaryPackages,
            *definition.applicationBoundaryWhitelist,
            *definition.sharedVocabularyPackages
    )
    return definition.applicationBoundaryPackages.flatMap {
        getRestrictiveRules(it, *allowedPackages)
    }
}


fun applicationImplRules(definition: BoundedContextDefinition): List<ArchRule> {
    val allowedPackages = arrayOf(
            *definition.whiteListPackages,
            *definition.applicationInteractorPackages,
            *definition.applicationInteractorWhitelist,
            *definition.applicationBoundaryPackages,
            *definition.domainModelPackages,
            *definition.domainServicesPackages,
            *definition.sharedVocabularyPackages
    )
    return definition.applicationInteractorPackages.flatMap {
        getRestrictiveRules(it, *allowedPackages)
    }
}

fun domainRules(definition: BoundedContextDefinition): List<ArchRule> {
    val allowedPackages = arrayOf(
            *definition.whiteListPackages,
            *definition.domainServicesPackages,
            *definition.domainServicesWhitelist,
            *definition.domainModelPackages,
            *definition.domainModelWhitelist,
            *definition.sharedVocabularyPackages)
    return listOf(*definition.domainModelPackages, *definition.domainServicesPackages)
            .flatMap { getRestrictiveRules(it, *allowedPackages) }
}

fun consumingInfraRules(definition: BoundedContextDefinition): List<ArchRule> {
    val allowedPackages = arrayOf(
            *definition.whiteListPackages,
            *definition.consumingInfrastructurePackages,
            *definition.consumingInfrastructureWhitelist,
            *definition.applicationBoundaryPackages,
            *definition.sharedVocabularyPackages)
    return definition.consumingInfrastructurePackages.flatMap {
        getRestrictiveRules(it, *allowedPackages)
    }
}


fun implementingInfraRules(definition: BoundedContextDefinition): List<ArchRule> {
    val allowedPackages = arrayOf(
            *definition.implementingInfrastructurePackages,
            *definition.whiteListPackages,
            *definition.implementingInfrastructureWhitelist,
            *definition.domainServicesPackages,
            *definition.domainModelPackages,
            *definition.sharedVocabularyPackages)
    return definition.implementingInfrastructurePackages.flatMap {
        getRestrictiveRules(it, *allowedPackages)
    }
}

fun getRestrictiveRules(sourcePackage: String, vararg allowedPackages: String): List<ArchRule> {
    return listOf(
            ArchRuleDefinition.classes()
                .that().resideInAPackage(sourcePackage)
                .should().onlyAccessClassesThat().resideInAnyPackage(*allowedPackages),
            ArchRuleDefinition.classes()
                .that().resideInAPackage(sourcePackage)
                .should().onlyDependOnClassesThat().resideInAnyPackage(*allowedPackages),
            ArchRuleDefinition.classes()
                .that().resideInAPackage(sourcePackage)
                .should(onlyBeAnnotatedWithClassesInPackage(*allowedPackages))
    )
}

fun onlyBeAnnotatedWithClassesInPackage(vararg allowedPackages: String): ArchCondition<JavaClass> {
    return OnlyAnnotatedWithClassesInPackage(*allowedPackages)
}

private class OnlyAnnotatedWithClassesInPackage internal constructor(private vararg val allowedPackages: String) : ArchCondition<JavaClass>("only be annotated with classes in packages [${allowedPackages.joinToString(", ")}]") {
    override fun check(item: JavaClass, events: ConditionEvents?) {
        val annotations = item.annotations
        annotations.forEach {
            val match = PackageMatchers.of(*allowedPackages).apply(it.rawType.`package`.name)
            if(!match) {
                events!!.violating.add(SimpleConditionEvent(item, false, "${item.name} has annotation that is not in allowed package: ${it.rawType.name}"))
            }
        }
        item.fields.forEach { field ->
            val fieldAnnotations = field.annotations
            fieldAnnotations.forEach {
                val match = PackageMatchers.of(*allowedPackages).apply(it.rawType.`package`.name)
                if(!match) {
                    events!!.violating.add(SimpleConditionEvent(item, false, "${item.name} has field (${field.name}) with annotation that is not in allowed package: ${it.rawType.name}"))
                }
            }
        }
        item.methods.forEach { method ->
            val fieldAnnotations = method.annotations
            fieldAnnotations.forEach {
                val match = PackageMatchers.of(*allowedPackages).apply(it.rawType.`package`.name)
                if(!match) {
                    events!!.violating.add(SimpleConditionEvent(item, false, "${item.name} has method (${method.name}) with annotation that is not in allowed package: ${it.rawType.name}"))
                }
            }
        }
    }
}

fun sharedVocabularyRules(definition: BoundedContextDefinition): List<ArchRule> {
    val allowedPackages = arrayOf(
            *definition.sharedVocabularyPackages,
            *definition.whiteListPackages,
            *definition.sharedVocabularyWhitelist
    )
    return definition.sharedVocabularyPackages.flatMap {
        getRestrictiveRules(it, *allowedPackages)
    }
}

fun mainPartitionRules(definition: BoundedContextDefinition): List<ArchRule> {
    val allowedPackages = arrayOf(
            *definition.whiteListPackages,
            *definition.mainPartitionPackages,
            *definition.mainPartitionWhitelist,
            *definition.implementingInfrastructurePackages,
            *definition.consumingInfrastructurePackages,
            *definition.applicationBoundaryPackages,
            *definition.applicationInteractorPackages,
            *definition.domainServicesPackages,
            *definition.domainModelPackages
    )
    return definition.mainPartitionPackages.flatMap {
        getRestrictiveRules(it, *allowedPackages)
    }
}

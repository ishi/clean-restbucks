package be.sourcedbvba.restbucks.order

import com.tngtech.archunit.base.PackageMatchers
import com.tngtech.archunit.core.domain.JavaClass
import com.tngtech.archunit.lang.ArchCondition
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.CompositeArchRule
import com.tngtech.archunit.lang.ConditionEvents
import com.tngtech.archunit.lang.SimpleConditionEvent
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition

fun CleanArchitectureDefinition.rule(): ArchRule {
    var cleanArchRule = CompositeArchRule.of(ArchRuleDefinition.classes().should().resideInAnyPackage(*this.boundedContexts.map { "${it.boundedContextPackage}.." }.toTypedArray()))
    this.boundedContexts.forEach { bc ->
        applicationApiRules(bc).forEach { cleanArchRule = cleanArchRule.and(it) }
        applicationImplRules(bc).forEach { cleanArchRule = cleanArchRule.and(it) }
        domainRules(bc).forEach { cleanArchRule = cleanArchRule.and(it) }
        consumingInfraRules(bc).forEach { cleanArchRule = cleanArchRule.and(it) }
        implementingInfraRules(bc).forEach { cleanArchRule = cleanArchRule.and(it) }
        sharedVocabularyRules(bc).forEach { cleanArchRule = cleanArchRule.and(it) }
        mainPartitionRules(bc).forEach { cleanArchRule = cleanArchRule.and(it) }
    }
    return cleanArchRule
}

fun CleanArchitectureDefinition.rules(): List<ArchRule> {
    val list = mutableListOf<ArchRule>()
    this.boundedContexts.forEach { bc ->
        applicationApiRules(bc).forEach { list.add(it) }
        applicationImplRules(bc).forEach { list.add(it) }
        domainRules(bc).forEach { list.add(it) }
        consumingInfraRules(bc).forEach { list.add(it) }
        implementingInfraRules(bc).forEach {list.add(it) }
        sharedVocabularyRules(bc).forEach { list.add(it) }
        mainPartitionRules(bc).forEach { list.add(it) }
    }
    return list
}


fun applicationApiRules(definition: BoundedContextDefinition): Array<ArchRule> {
    val list = mutableListOf<ArchRule>()
    val allowedPackages = arrayOf(
            *definition.whiteListPackages,
            *definition.applicationBoundaryPackages,
            *definition.applicationBoundaryWhitelist,
            *definition.sharedVocabularyPackages,
            *definition.sharedVocabularyWhitelist
    )
    definition.applicationBoundaryPackages.map {
        getRestrictiveRules(it, *allowedPackages)
    }.forEach {
        list.addAll(it)
    }
    return list.toTypedArray()
}


fun applicationImplRules(definition: BoundedContextDefinition): Array<ArchRule> {
    val list = mutableListOf<ArchRule>()
    val allowedPackages = arrayOf(
            *definition.applicationBoundaryPackages,
            *definition.applicationInteractorPackages,
            *definition.domainModelPackages,
            *definition.domainServicesPackages,
            *definition.whiteListPackages,
            *definition.applicationBoundaryWhitelist,
            *definition.applicationInteractorWhitelist,
            *definition.sharedVocabularyPackages,
            *definition.sharedVocabularyWhitelist
    )
    definition.applicationInteractorPackages.map {
        getRestrictiveRules(it, *allowedPackages)
    }.forEach {
        list.addAll(it)
    }
    return list.toTypedArray()
}

fun domainRules(definition: BoundedContextDefinition): Array<ArchRule> {
    val list = mutableListOf<ArchRule>()
    val allowedPackages = arrayOf(
            *definition.domainServicesPackages,
            *definition.domainServicesWhitelist,
            *definition.domainModelPackages,
            *definition.domainModelWhitelist,
            *definition.whiteListPackages,
            *definition.sharedVocabularyPackages,
            *definition.sharedVocabularyWhitelist)
    definition.domainModelPackages.forEach {
        list.addAll(getRestrictiveRules(it, *allowedPackages))
    }
    definition.domainServicesPackages.forEach {
        list.addAll(getRestrictiveRules(it, *allowedPackages))
    }
    return list.toTypedArray()
}

fun consumingInfraRules(definition: BoundedContextDefinition): Array<ArchRule> {
    val list = mutableListOf<ArchRule>()
    definition.consumingInfrastructurePackages.forEach {
        val allowedPackages = arrayOf(*definition.implementingInfrastructurePackages,
                *definition.applicationBoundaryPackages,
                *definition.consumingInfrastructurePackages,
                *definition.consumingInfrastructureWhitelist,
                *definition.whiteListPackages,
                *definition.sharedVocabularyPackages,
                *definition.sharedVocabularyWhitelist)
        list.addAll(getRestrictiveRules(it, *allowedPackages))

    }
    return list.toTypedArray()
}


fun implementingInfraRules(definition: BoundedContextDefinition): Array<ArchRule> {
    val list = mutableListOf<ArchRule>()
    val allowedPackages = arrayOf(*definition.implementingInfrastructurePackages,
            *definition.implementingInfrastructureWhitelist,
            *definition.domainServicesPackages,
            *definition.domainServicesWhitelist,
            *definition.domainModelPackages,
            *definition.domainModelWhitelist,
            *definition.whiteListPackages,
            *definition.sharedVocabularyPackages,
            *definition.sharedVocabularyWhitelist)
    definition.implementingInfrastructurePackages.map {
        getRestrictiveRules(it, *allowedPackages)
    }.forEach {
        list.addAll(it)
    }
   return list.toTypedArray()
}

fun getRestrictiveRules(sourcePackage: String, vararg allowedPackages: String): List<ArchRule> {
    val list = mutableListOf<ArchRule>()
    list.add(ArchRuleDefinition.classes()
            .that().resideInAPackage(sourcePackage)
            .should().onlyAccessClassesThat().resideInAnyPackage(*allowedPackages))
    list.add(ArchRuleDefinition.classes()
            .that().resideInAPackage(sourcePackage)
            .should().onlyDependOnClassesThat().resideInAnyPackage(*allowedPackages))
    list.add(ArchRuleDefinition.classes()
            .that().resideInAPackage(sourcePackage)
            .should(onlyBeAnnotatedWithClassesInPackage(*allowedPackages)))
    return list
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

fun sharedVocabularyRules(definition: BoundedContextDefinition): Array<ArchRule> {
    val list = mutableListOf<ArchRule>()
    val allowedPackages = arrayOf(
            *definition.sharedVocabularyPackages,
            *definition.whiteListPackages,
            *definition.sharedVocabularyWhitelist
    )
    definition.sharedVocabularyPackages.map {
        getRestrictiveRules(it, *allowedPackages)
    }.forEach {
        list.addAll(it)
    }
    return list.toTypedArray()
}

fun mainPartitionRules(definition: BoundedContextDefinition): Array<ArchRule> {
    val list = mutableListOf<ArchRule>()
    val allowedPackages = arrayOf(
            *definition.whiteListPackages,
            *definition.mainPartitionPackages,
            *definition.mainPartitionWhitelist,
            *definition.implementingInfrastructurePackages,
            *definition.implementingInfrastructureWhitelist,
            *definition.consumingInfrastructurePackages,
            *definition.consumingInfrastructureWhitelist,
            *definition.applicationBoundaryPackages,
            *definition.applicationBoundaryWhitelist,
            *definition.applicationInteractorPackages,
            *definition.applicationInteractorWhitelist,
            *definition.domainServicesPackages,
            *definition.domainServicesWhitelist,
            *definition.domainModelPackages,
            *definition.domainModelWhitelist
    )
    definition.mainPartitionPackages.map {
        getRestrictiveRules(it, *allowedPackages)
    }.forEach {
        list.addAll(it)
    }
    return list.toTypedArray()
}

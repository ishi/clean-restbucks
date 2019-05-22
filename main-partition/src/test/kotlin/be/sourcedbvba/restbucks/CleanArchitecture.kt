package be.sourcedbvba.restbucks

import com.tngtech.archunit.core.domain.JavaClasses

class CleanArchitectureBuilder(val boundedContextPackage: String){
   val definition: CleanArchitectureDefinition = CleanArchitectureDefinition(boundedContextPackage)

    fun application(body: ApplicationLayerBuilder.() -> Unit) {
        ApplicationLayerBuilder(definition).apply(body)
    }

    fun domain(body: DomainLayerBuilder.() -> Unit) {
        DomainLayerBuilder(definition).apply(body)
    }

    fun infrastructure(body: InfrastructureLayerBuilder.() -> Unit) {
        InfrastructureLayerBuilder(definition).apply(body)
    }

    fun shared(body: SharedLayerBuilder.() -> Unit) {
        SharedLayerBuilder(definition).apply(body)
    }

    fun mainPartition(body: () -> String) {
        definition.mainPartitionSubPackage = body()
    }
}

class ApplicationLayerBuilder(val definition: CleanArchitectureDefinition){
    fun boundary(body: () -> String) {
        definition.applicationBoundarySubPackage = body()
    }

    fun interactor(body: () -> String) {
        definition.applicationInteractorSubPackage = body()
    }
}

class DomainLayerBuilder(val definition: CleanArchitectureDefinition){
    fun model(body: () -> String) {
        definition.domainModelSubPackage = body()
    }

    fun services(body: () -> String) {
        definition.domainServicesSubPackage = body()
    }
}

class InfrastructureLayerBuilder(val definition: CleanArchitectureDefinition){
    fun consuming(body: () -> String) {
        definition.consumingInfrastructureSubPackage = body()
    }

    fun implementing(body: () -> String) {
        definition.implementingInfrastructureSubPackage = body()
    }
}

class SharedLayerBuilder(val definition: CleanArchitectureDefinition){
    fun vocabulary(body: () -> String) {
        definition.sharedVocabularySubPackage = body()
    }
}

fun cleanArchitecture(boundedContext: String, body: CleanArchitectureBuilder.() -> Unit): CleanArchitectureDefinition {
    return CleanArchitectureBuilder(boundedContext).apply(body).definition
}


val structure = cleanArchitecture("be.sourcedbvba.restbucks") {
    application {
        boundary { "app.api" }
        interactor { "app.impl" }
    }
    domain {
        model { "domain.model" }
        services { "domain.services" }
    }
    infrastructure {
        consuming { "infra.web" }
        implementing { "infra.persistence" }
    }
    shared {
        vocabulary { "shared.vocabulary" }
    }
    mainPartition { "main" }
}



class CleanArchitectureDefinition(val boundedContextPackage: String) {
    lateinit var domainModelSubPackage: String
    lateinit var domainServicesSubPackage: String
    lateinit var consumingInfrastructureSubPackage: String
    lateinit var implementingInfrastructureSubPackage: String
    lateinit var applicationBoundarySubPackage: String
    lateinit var applicationInteractorSubPackage: String
    lateinit var sharedVocabularySubPackage: String
    lateinit var mainPartitionSubPackage: String
}

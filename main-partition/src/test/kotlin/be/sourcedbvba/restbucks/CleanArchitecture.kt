package be.sourcedbvba.restbucks

class CleanArchitectureBuilder {
   val definition: CleanArchitectureDefinition = CleanArchitectureDefinition()

    fun boundedContext(boundedContextPackage: String, body: BoundedContextBuilder.() -> Unit) {
        definition.boundedContexts.add(BoundedContextBuilder(boundedContextPackage).apply(body).definition)
    }
}

class BoundedContextBuilder(boundedContextPackage: String) {
    val definition: BoundedContextDefinition = BoundedContextDefinition(boundedContextPackage)

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
        definition.mainPartitionSubPackages.add(body())
    }
}

class ApplicationLayerBuilder(val definition: BoundedContextDefinition){
    fun boundary(body: () -> String) {
        definition.applicationBoundarySubPackages.add(body())
    }

    fun interactor(body: () -> String) {
        definition.applicationInteractorSubPackages.add(body())
    }
}

class DomainLayerBuilder(val definition: BoundedContextDefinition){
    fun model(body: () -> String) {
        definition.domainModelSubPackages.add(body())
    }

    fun services(body: () -> String) {
        definition.domainServicesSubPackages.add(body())
    }
}

class InfrastructureLayerBuilder(val definition: BoundedContextDefinition){
    fun consuming(body: () -> String) {
        definition.consumingInfrastructureSubPackages.add(body())
    }

    fun implementing(body: () -> String) {
        definition.implementingInfrastructureSubPackages.add(body())
    }
}

class SharedLayerBuilder(val definition: BoundedContextDefinition){
    fun vocabulary(body: () -> String) {
        definition.sharedVocabularySubPackages.add(body())
    }
}

fun cleanArchitecture(body: CleanArchitectureBuilder.() -> Unit): CleanArchitectureDefinition {
    return CleanArchitectureBuilder().apply(body).definition
}


class BoundedContextDefinition(val boundedContextPackage: String) {
    val domainModelSubPackages: MutableList<String> = mutableListOf()
    val domainServicesSubPackages: MutableList<String> = mutableListOf()
    val consumingInfrastructureSubPackages: MutableList<String> = mutableListOf()
    val implementingInfrastructureSubPackages: MutableList<String> = mutableListOf()
    val applicationBoundarySubPackages: MutableList<String> = mutableListOf()
    val applicationInteractorSubPackages: MutableList<String> = mutableListOf()
    val sharedVocabularySubPackages: MutableList<String> = mutableListOf()
    val mainPartitionSubPackages: MutableList<String> = mutableListOf()

    val domainModelPackages: Array<String>
        get() = domainModelSubPackages.map { "$boundedContextPackage.$it" }.toTypedArray()

    val domainServicesPackages: Array<String>
        get() = domainModelSubPackages.map { "$boundedContextPackage.$it" }.toTypedArray()

    val consumingInfrastructurePackages: Array<String>
        get() = consumingInfrastructureSubPackages.map { "$boundedContextPackage.$it" }.toTypedArray()

    val implementingInfrastructurePackages: Array<String>
        get() = implementingInfrastructureSubPackages.map { "$boundedContextPackage.$it" }.toTypedArray()

    val applicationBoundaryPackages: Array<String>
        get() = applicationBoundarySubPackages.map { "$boundedContextPackage.$it" }.toTypedArray()

    val applicationInteractorPackages: Array<String>
        get() = applicationInteractorSubPackages.map { "$boundedContextPackage.$it" }.toTypedArray()

    val sharedVocabularyPackages: Array<String>
        get() = sharedVocabularySubPackages.map { "$boundedContextPackage.$it" }.toTypedArray()

    val mainPartitionPackages: Array<String>
        get() = mainPartitionSubPackages.map { "$boundedContextPackage.$it" }.toTypedArray()
}

class CleanArchitectureDefinition() {
    val boundedContexts: MutableList<BoundedContextDefinition> = mutableListOf();
}

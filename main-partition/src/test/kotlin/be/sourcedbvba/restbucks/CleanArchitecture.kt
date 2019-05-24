package be.sourcedbvba.restbucks

class CleanArchitectureBuilder {
   val definition: CleanArchitectureDefinition = CleanArchitectureDefinition()

    fun boundedContext(boundedContextPackage: String, body: BoundedContextBuilder.() -> Unit) {
        definition.boundedContexts.add(BoundedContextBuilder(boundedContextPackage).apply(body).build())
    }
}

class BoundedContextBuilder(boundedContextPackage: String) {
    private val definition: BoundedContextDefinition = BoundedContextDefinition(boundedContextPackage)
    lateinit var whiteList: List<String>

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

    fun mainPartition(body: ModuleBuilder.() -> Unit) {
        definition.mainPartition.add(ModuleBuilder().apply(body).moduleDefinition)
    }

    fun build(): BoundedContextDefinition {
        definition.whiteList.addAll(whiteList)
        return definition
    }
}

class ApplicationLayerBuilder(val definition: BoundedContextDefinition){
    fun boundary(body: ModuleBuilder.() -> Unit) {
        definition.applicationBoundary.add(ModuleBuilder().apply(body).moduleDefinition)
    }

    fun interactor(body: ModuleBuilder.() -> Unit) {
        definition.applicationInteractor.add(ModuleBuilder().apply(body).moduleDefinition)
    }
}

class DomainLayerBuilder(val definition: BoundedContextDefinition){
    fun model(body: ModuleBuilder.() -> Unit) {
        definition.domainModel.add(ModuleBuilder().apply(body).moduleDefinition)
    }

    fun services(body: ModuleBuilder.() -> Unit) {
        definition.domainServices.add(ModuleBuilder().apply(body).moduleDefinition)
    }
}

class InfrastructureLayerBuilder(val definition: BoundedContextDefinition){
    fun consuming(body: ModuleBuilder.() -> Unit) {
        definition.consumingInfrastructure.add(ModuleBuilder().apply(body).moduleDefinition)
    }

    fun implementing(body: ModuleBuilder.() -> Unit) {
        definition.implementingInfrastructure.add(ModuleBuilder().apply(body).moduleDefinition)
    }
}

class SharedLayerBuilder(val definition: BoundedContextDefinition){
    fun vocabulary(body: ModuleBuilder.() -> Unit) {
        definition.sharedVocabulary.add(ModuleBuilder().apply(body).moduleDefinition)
    }
}

class ModuleBuilder {
    lateinit var subPackage: String
    var whiteList: List<String> = listOf()

    val moduleDefinition: ModuleDefinition
        get() = ModuleDefinition(subPackage, whiteList)
}

fun cleanArchitecture(body: CleanArchitectureBuilder.() -> Unit): CleanArchitectureDefinition {
    return CleanArchitectureBuilder().apply(body).definition
}

data class ModuleDefinition(val subPackage: String, val whiteListedPackages: List<String>)

class BoundedContextDefinition(val boundedContextPackage: String) {
    val whiteList: MutableList<String> = mutableListOf()
    val domainModel: MutableList<ModuleDefinition> = mutableListOf()
    val domainServices: MutableList<ModuleDefinition> = mutableListOf()
    val consumingInfrastructure: MutableList<ModuleDefinition> = mutableListOf()
    val implementingInfrastructure: MutableList<ModuleDefinition> = mutableListOf()
    val applicationBoundary: MutableList<ModuleDefinition> = mutableListOf()
    val applicationInteractor: MutableList<ModuleDefinition> = mutableListOf()
    val sharedVocabulary: MutableList<ModuleDefinition> = mutableListOf()
    val mainPartition: MutableList<ModuleDefinition> = mutableListOf()

    val domainModelPackages: Array<String>
        get() = domainModel.map { "$boundedContextPackage.${it.subPackage}" }.toTypedArray()

    val domainModelWhitelist: Array<String>
        get() = domainModel.flatMap { it.whiteListedPackages }.toTypedArray()

    val domainServicesPackages: Array<String>
        get() = domainServices.map { "$boundedContextPackage.${it.subPackage}" }.toTypedArray()

    val domainServicesWhitelist: Array<String>
        get() = domainServices.flatMap { it.whiteListedPackages }.toTypedArray()

    val consumingInfrastructurePackages: Array<String>
        get() = consumingInfrastructure.map { "$boundedContextPackage.${it.subPackage}" }.toTypedArray()

    val consumingInfrastructureWhitelist: Array<String>
        get() = consumingInfrastructure.flatMap { it.whiteListedPackages }.toTypedArray()

    val implementingInfrastructurePackages: Array<String>
        get() = implementingInfrastructure.map { "$boundedContextPackage.${it.subPackage}" }.toTypedArray()

    val implementingInfrastructureWhitelist: Array<String>
        get() = implementingInfrastructure.flatMap { it.whiteListedPackages }.toTypedArray()

    val applicationBoundaryPackages: Array<String>
        get() = applicationBoundary.map { "$boundedContextPackage.${it.subPackage}" }.toTypedArray()

    val applicationBoundaryWhitelist: Array<String>
        get() = applicationBoundary.flatMap { it.whiteListedPackages }.toTypedArray()

    val applicationInteractorPackages: Array<String>
        get() = applicationInteractor.map { "$boundedContextPackage.${it.subPackage}" }.toTypedArray()

    val applicationInteractorWhitelist: Array<String>
        get() = applicationBoundary.flatMap { it.whiteListedPackages }.toTypedArray()

    val sharedVocabularyPackages: Array<String>
        get() = sharedVocabulary.map { "$boundedContextPackage.${it.subPackage}" }.toTypedArray()

    val sharedVocabularyWhitelist: Array<String>
        get() = sharedVocabulary.flatMap { it.whiteListedPackages }.toTypedArray()

    val mainPartitionPackages: Array<String>
        get() = mainPartition.map { "$boundedContextPackage.${it.subPackage}" }.toTypedArray()

    val mainPartitionWhitelist: Array<String>
        get() = mainPartition.flatMap { it.whiteListedPackages }.toTypedArray()

    val whiteListPackages: Array<String>
        get() = whiteList.toTypedArray()

}

class CleanArchitectureDefinition() {
    val boundedContexts: MutableList<BoundedContextDefinition> = mutableListOf();
}

package be.sourcedbvba.restbucks

import com.tngtech.archunit.core.importer.ClassFileImporter
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CleanArchitectureTests {
    private val classes = ClassFileImporter().importPackages("be.sourcedbvba.restbucks.order..")

    @Test
    fun validateArchitecture() {
        cleanArchitecture {
            boundedContext("be.sourcedbvba.restbucks.order") {
                whiteList = listOf("java.lang..", "java.util..", "java.math..", "kotlin..", "org.jetbrains.annotations..")

                application {
                    boundary {
                        subPackage = "api.."
                    }
                    interactor {
                        subPackage = "impl.."
                    }
                }
                domain {
                    model {
                        subPackage = "domain.model.."
                    }
                    services {
                        subPackage = "domain.services.."
                    }
                }
                infrastructure {
                    consuming {
                        subPackage = "infra.web.."
                        whiteList = listOf("org.springframework.web..", "org.springframework.http..", "reactor.core..")
                    }
                    implementing {
                        subPackage = "infra.persistence.."
                        whiteList = listOf("org.springframework.transaction..", "org.aspectj..", "org.springframework.data..", "javax.persistence..")
                    }
                }
                shared {
                    vocabulary {
                        subPackage = "shared.vocabulary.."
                    }
                }
                mainPartition {
                    subPackage = "main.."
                }
            }
        }.rule().check(classes)
    }
}

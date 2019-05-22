package be.sourcedbvba.restbucks

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ClassFileImporter
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CleanArchitectureTests {
    lateinit var importedClasses: JavaClasses

    @BeforeAll
    fun importClasses() {
        importedClasses = ClassFileImporter().importPackages("be.sourcedbvba.restbucks.order")
    }

    @Test
    fun validateArchitecture() {
        val structure = cleanArchitecture {
            boundedContext("be.sourcedbvba.restbucks.order") {
                application {
                    boundary { "api" }
                    interactor { "impl" }
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
        }
        structure.check(importedClasses)
    }
}

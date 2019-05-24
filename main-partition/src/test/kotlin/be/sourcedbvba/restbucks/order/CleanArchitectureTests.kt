package be.sourcedbvba.restbucks.order

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.lang.ArchRule
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CleanArchitectureTests {
    private val classes = ClassFileImporter().importPackages("be.sourcedbvba.restbucks.order..")

    val architecture = cleanArchitecture {
        boundedContext("be.sourcedbvba.restbucks.order") {
            whiteList = listOf(
                    "java.lang..",
                    "java.util..",
                    "java.math..",
                    "kotlin..",
                    "org.jetbrains.annotations.."
            )

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
                    whiteList = listOf(
                            "org.springframework.web..",
                            "org.springframework.http..",
                            "reactor.core.."
                    )
                }
                implementing {
                    subPackage = "infra.persistence.."
                    whiteList = listOf(
                            "org.springframework.transaction..",
                            "org.aspectj..",
                            "org.springframework.data..",
                            "javax.persistence.."
                    )
                }
            }
            shared {
                vocabulary {
                    subPackage = "shared.vocabulary.."
                }
            }
            mainPartition {
                subPackage = "main.."
                whiteList = listOf(
                        "org.springframework.context..",
                        "org.springframework.orm..",
                        "org.springframework.core..",
                        "org.springframework.boot..",
                        "org.springframework.scheduling..",
                        "org.springframework.transaction..",
                        "org.springframework.web..",
                        "javax.."
                )
            }
        }
    }

    @ParameterizedTest(name = "{index}: {1}")
    @MethodSource("ruleParametersProvider")
    fun `validate architecture`(rule: ArchRule, name: String) {
        rule.check(classes)
    }

    private fun ruleParametersProvider(): List<Arguments> {
        return architecture.rules().map { Arguments.of(it, it.description) }
    }
}

package be.sourcedbvba.restbucks.domain.transaction

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableTransactionManagement
class UseCaseTransactionConfiguration {
    @Bean
    fun transactionalUseCaseExecutor() = TransactionalExecutor()

    fun transactionalRunner(executor: TransactionalExecutor) = SpringTransactionalRunner(executor)
}
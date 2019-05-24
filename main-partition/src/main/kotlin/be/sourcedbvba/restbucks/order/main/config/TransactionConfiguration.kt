package be.sourcedbvba.restbucks.order.main.config

import be.sourcedbvba.restbucks.order.infra.persistence.transaction.TransactionalUseCaseAspect
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableTransactionManagement
@EnableAspectJAutoProxy
class TransactionConfiguration {
    @Bean
    fun useCaseTransactionAspect(transactionManager: PlatformTransactionManager) = TransactionalUseCaseAspect(transactionManager)
}

package be.sourcedbvba.restbucks.domain.transaction

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.transaction.support.TransactionTemplate

@Configuration
@EnableTransactionManagement
class TransactionConfiguration {
    @Bean
    fun transactionFactory(transactionManager: PlatformTransactionManager) = SpringTransactionFactory(transactionManager)
}
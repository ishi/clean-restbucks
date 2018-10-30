package be.sourcedbvba.restbucks.domain.transaction

import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.DefaultTransactionDefinition

class SpringTransactionFactory(private val transactionManager: PlatformTransactionManager) : TransactionFactory {
    override fun start() : Transaction {
        return SpringTransaction(transactionManager, transactionManager.getTransaction(DefaultTransactionDefinition()))
    }
}
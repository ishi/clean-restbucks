package be.sourcedbvba.restbucks.domain.transaction

import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionStatus

class SpringTransaction(private val transactionManager: PlatformTransactionManager,
                        private val transactionStatus: TransactionStatus) : Transaction {
    override fun commit() {
        transactionManager.commit(transactionStatus)
    }

    override fun rollback() {
        transactionManager.rollback(transactionStatus)
    }
}
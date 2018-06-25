package be.sourcedbvba.restbucks.domain.transaction

import org.springframework.transaction.support.TransactionTemplate
import java.util.function.Supplier

class SpringTransactionalRunner(private val transactionTemplate: TransactionTemplate) : TransactionalRunner {
    override fun <T> runInTransaction(method: Supplier<T>): T? {
        return transactionTemplate.execute { method.get() }
    }

}
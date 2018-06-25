package be.sourcedbvba.restbucks.domain.transaction

import java.util.function.Supplier

interface TransactionalRunner {
    fun <T> runInTransaction(method: Supplier<T>) : T?
}
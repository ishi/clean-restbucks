package be.sourcedbvba.restbucks.domain.transaction

interface TransactionFactory {
    fun start() : Transaction
}
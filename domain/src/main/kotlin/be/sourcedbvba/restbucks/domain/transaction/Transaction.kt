package be.sourcedbvba.restbucks.domain.transaction

interface Transaction {
    fun commit()
    fun rollback()
}

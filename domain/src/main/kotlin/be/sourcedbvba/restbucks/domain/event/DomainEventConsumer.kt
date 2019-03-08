package be.sourcedbvba.restbucks.domain.event

interface DomainEventConsumer {
    fun canHandle(event: DomainEvent): Boolean
    fun consume(event: DomainEvent)
}

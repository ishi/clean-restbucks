package be.sourcedbvba.restbucks.order.domain.services.event

interface DomainEventConsumer {
    fun canHandle(event: DomainEvent): Boolean
    fun consume(event: DomainEvent)
}

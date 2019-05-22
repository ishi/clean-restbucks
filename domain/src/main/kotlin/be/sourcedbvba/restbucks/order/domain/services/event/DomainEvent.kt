package be.sourcedbvba.restbucks.order.domain.services.event

interface DomainEvent {
    fun sendEvent() {
        return EventPublisher.Locator.eventPublisher(this)
    }
}

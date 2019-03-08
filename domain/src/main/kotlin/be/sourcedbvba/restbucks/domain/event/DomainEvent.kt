package be.sourcedbvba.restbucks.domain.event

interface DomainEvent {
    fun sendEvent() {
        return EventPublisher.Locator.eventPublisher(this)
    }
}

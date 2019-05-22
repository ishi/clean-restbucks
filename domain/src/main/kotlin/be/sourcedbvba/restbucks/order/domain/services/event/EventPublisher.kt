package be.sourcedbvba.restbucks.order.domain.services.event

interface EventPublisher {
    object Locator {
        lateinit var eventPublisher: (event: DomainEvent) -> Unit
    }
}

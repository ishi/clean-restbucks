package be.sourcedbvba.restbucks.domain.event

interface EventPublisher {
    object Locator {
        lateinit var eventPublisher: (event: DomainEvent) -> Unit
    }
}

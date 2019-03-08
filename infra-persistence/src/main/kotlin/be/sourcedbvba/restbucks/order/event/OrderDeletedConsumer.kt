package be.sourcedbvba.restbucks.order.event

import be.sourcedbvba.restbucks.domain.event.DomainEvent
import be.sourcedbvba.restbucks.domain.event.DomainEventConsumer
import be.sourcedbvba.restbucks.order.gateway.jpa.OrderRepository

class OrderDeletedConsumer constructor(private val orderRepository: OrderRepository) : DomainEventConsumer {
    override fun canHandle(event: DomainEvent) = event is OrderDeleted

    override fun consume(event: DomainEvent) {
        orderRepository.deleteById((event as OrderDeleted).getId().value)
    }
}

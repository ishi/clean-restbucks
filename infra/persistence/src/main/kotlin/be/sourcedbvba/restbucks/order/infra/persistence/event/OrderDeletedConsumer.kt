package be.sourcedbvba.restbucks.order.infra.persistence.event

import be.sourcedbvba.restbucks.order.domain.services.event.DomainEvent
import be.sourcedbvba.restbucks.order.domain.services.event.DomainEventConsumer
import be.sourcedbvba.restbucks.order.domain.services.event.OrderDeleted
import be.sourcedbvba.restbucks.order.infra.persistence.gateway.jpa.OrderRepository

class OrderDeletedConsumer constructor(private val orderRepository: OrderRepository) : DomainEventConsumer {
    override fun canHandle(event: DomainEvent) = event is OrderDeleted

    override fun consume(event: DomainEvent) {
        orderRepository.deleteById((event as OrderDeleted).getId().value)
    }
}

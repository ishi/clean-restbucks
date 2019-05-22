package be.sourcedbvba.restbucks.order.infra.persistence.event

import be.sourcedbvba.restbucks.order.shared.vocabulary.Status
import be.sourcedbvba.restbucks.order.domain.services.event.DomainEvent
import be.sourcedbvba.restbucks.order.domain.services.event.DomainEventConsumer
import be.sourcedbvba.restbucks.order.domain.services.event.OrderDelivered
import be.sourcedbvba.restbucks.order.infra.persistence.gateway.jpa.OrderRepository
import java.lang.IllegalArgumentException

class OrderDeliveredConsumer constructor(private val orderRepository: OrderRepository) : DomainEventConsumer {
    override fun canHandle(event: DomainEvent) = event is OrderDelivered

    override fun consume(event: DomainEvent) {
        val order = orderRepository.findById((event as OrderDelivered).getId().value).orElseThrow { IllegalArgumentException() }
        order.status = Status.DELIVERED
        orderRepository.save(order)
    }
}

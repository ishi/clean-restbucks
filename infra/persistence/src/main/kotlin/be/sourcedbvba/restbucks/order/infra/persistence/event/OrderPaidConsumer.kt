package be.sourcedbvba.restbucks.order.infra.persistence.event

import be.sourcedbvba.restbucks.order.shared.vocabulary.Status
import be.sourcedbvba.restbucks.order.domain.services.event.DomainEvent
import be.sourcedbvba.restbucks.order.domain.services.event.DomainEventConsumer
import be.sourcedbvba.restbucks.order.domain.services.event.OrderPaid
import be.sourcedbvba.restbucks.order.infra.persistence.gateway.jpa.OrderRepository
import java.lang.IllegalArgumentException

class OrderPaidConsumer constructor(private val orderRepository: OrderRepository) : DomainEventConsumer {
    override fun canHandle(event: DomainEvent) = event is OrderPaid

    override fun consume(event: DomainEvent) {
        val order = orderRepository.findById((event as OrderPaid).getId().value).orElseThrow { IllegalArgumentException() }
        order.status = Status.PAID
        orderRepository.save(order)
    }
}

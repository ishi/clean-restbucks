package be.sourcedbvba.restbucks.order.event

import be.sourcedbvba.restbucks.Status
import be.sourcedbvba.restbucks.domain.event.DomainEvent
import be.sourcedbvba.restbucks.domain.event.DomainEventConsumer
import be.sourcedbvba.restbucks.order.gateway.jpa.OrderRepository
import java.lang.IllegalArgumentException

class OrderDeliveredConsumer constructor(private val orderRepository: OrderRepository) : DomainEventConsumer {
    override fun canHandle(event: DomainEvent) = event is OrderDelivered

    override fun consume(event: DomainEvent) {
        val order = orderRepository.findById((event as OrderDelivered).getId().value).orElseThrow { IllegalArgumentException() }
        order.status = Status.DELIVERED
        orderRepository.save(order)
    }
}

package be.sourcedbvba.restbucks.order.event

import be.sourcedbvba.restbucks.Status
import be.sourcedbvba.restbucks.domain.event.DomainEvent
import be.sourcedbvba.restbucks.domain.event.DomainEventConsumer
import be.sourcedbvba.restbucks.order.gateway.jpa.OrderRepository
import java.lang.IllegalArgumentException

class OrderPaidConsumer constructor(private val orderRepository: OrderRepository) : DomainEventConsumer {
    override fun canHandle(event: DomainEvent) = event is OrderPaid

    override fun consume(event: DomainEvent) {
        val order = orderRepository.findById((event as OrderPaid).getId().value).orElseThrow { IllegalArgumentException() }
        order.status = Status.PAID
        orderRepository.save(order)
    }
}

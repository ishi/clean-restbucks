package be.sourcedbvba.restbucks.order.event

import be.sourcedbvba.restbucks.Status
import be.sourcedbvba.restbucks.domain.event.*
import be.sourcedbvba.restbucks.order.gateway.OrderRepository
import org.springframework.stereotype.Component

@Component
internal class OrderPaidConsumer internal constructor(private val orderRepository: OrderRepository) : DomainEventConsumer<OrderPaid> {
    override fun consume(event: OrderPaid) {
        val order = orderRepository.getOne(event.getId().value)
        order.status = Status.PAID
        orderRepository.save(order)
    }
}

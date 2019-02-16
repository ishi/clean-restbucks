package be.sourcedbvba.restbucks.order.event

import be.sourcedbvba.restbucks.Status
import be.sourcedbvba.restbucks.domain.event.DomainEventConsumer
import be.sourcedbvba.restbucks.order.gateway.OrderRepository
import org.springframework.stereotype.Component

@Component
internal class OrderDeliveredConsumer internal constructor(private val orderRepository: OrderRepository) : DomainEventConsumer<OrderDelivered> {
    override fun consume(event: OrderDelivered) {
        val order = orderRepository.getOne(event.getId().value)
        order.status = Status.DELIVERED
        orderRepository.save(order)
    }
}

package be.sourcedbvba.restbucks.order.event

import be.sourcedbvba.restbucks.Status
import be.sourcedbvba.restbucks.domain.event.DomainEventConsumer
import be.sourcedbvba.restbucks.order.gateway.OrderJpaRepository
import org.springframework.stereotype.Component

@Component
internal class OrderDeliveredConsumer internal constructor(private val orderJpaRepository: OrderJpaRepository) : DomainEventConsumer<OrderDelivered> {
    override fun consume(event: OrderDelivered) {
        val order = orderJpaRepository.getOne(event.getId().value)
        order.status = Status.DELIVERED
        orderJpaRepository.save(order)
    }
}

package be.sourcedbvba.restbucks.order.event

import be.sourcedbvba.restbucks.Status
import be.sourcedbvba.restbucks.domain.event.*
import be.sourcedbvba.restbucks.order.gateway.OrderJpaRepository
import org.springframework.stereotype.Component

@Component
internal class OrderPaidConsumer internal constructor(private val orderJpaRepository: OrderJpaRepository) : DomainEventConsumer<OrderPaid> {
    override fun consume(event: OrderPaid) {
        val order = orderJpaRepository.getOne(event.getId().value)
        order.status = Status.PAID
        orderJpaRepository.save(order)
    }
}

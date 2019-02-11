package be.sourcedbvba.restbucks.order.event

import be.sourcedbvba.restbucks.domain.event.DomainEventConsumer
import be.sourcedbvba.restbucks.order.*
import be.sourcedbvba.restbucks.order.gateway.OrderRepository
import org.springframework.stereotype.Component

@Component
internal class OrderCreatedConsumer internal constructor(private val orderRepository: OrderRepository) : DomainEventConsumer<OrderCreated> {
    override fun consume(event: OrderCreated) {
        val orderEntity = event.getOrder().toJpa()
        orderRepository.save(orderEntity)
    }

    internal fun OrderState.toJpa() : OrderEntity {
        return OrderEntity(id.value, customer, status, cost, items.map { it.toJpa() })
    }

    internal fun OrderItemState.toJpa() : OrderItemEntity {
        return OrderItemEntity(null, productName, quantity, size, milk)
    }
}

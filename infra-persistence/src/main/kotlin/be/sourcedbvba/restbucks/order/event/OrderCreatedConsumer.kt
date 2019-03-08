package be.sourcedbvba.restbucks.order.event

import be.sourcedbvba.restbucks.domain.event.DomainEvent
import be.sourcedbvba.restbucks.domain.event.DomainEventConsumer
import be.sourcedbvba.restbucks.order.gateway.jpa.OrderEntity
import be.sourcedbvba.restbucks.order.gateway.jpa.OrderItemEntity
import be.sourcedbvba.restbucks.order.gateway.jpa.OrderRepository

class OrderCreatedConsumer constructor(private val orderRepository: OrderRepository) : DomainEventConsumer {
    override fun canHandle(event: DomainEvent) = event is OrderCreated

    override fun consume(event: DomainEvent) {
        val orderEntity = (event as OrderCreated).getOrder().toJpa()
        orderRepository.save(orderEntity)
    }

    internal fun OrderState.toJpa(): OrderEntity {
        return OrderEntity(id.value, customer, status, cost, items.map { it.toJpa() })
    }

    internal fun OrderItemState.toJpa(): OrderItemEntity {
        return OrderItemEntity(null, productName, quantity, size, milk)
    }
}

package be.sourcedbvba.restbucks.order.infra.persistence.event

import be.sourcedbvba.restbucks.order.domain.services.event.DomainEvent
import be.sourcedbvba.restbucks.order.domain.services.event.DomainEventConsumer
import be.sourcedbvba.restbucks.order.domain.services.event.OrderCreated
import be.sourcedbvba.restbucks.order.domain.services.event.OrderItemState
import be.sourcedbvba.restbucks.order.domain.services.event.OrderState
import be.sourcedbvba.restbucks.order.infra.persistence.gateway.jpa.OrderEntity
import be.sourcedbvba.restbucks.order.infra.persistence.gateway.jpa.OrderItemEntity
import be.sourcedbvba.restbucks.order.infra.persistence.gateway.jpa.OrderRepository
import java.util.UUID

class OrderCreatedConsumer constructor(private val orderRepository: OrderRepository) : DomainEventConsumer {
    override fun canHandle(event: DomainEvent) = event is OrderCreated

    override fun consume(event: DomainEvent) {
        val orderEntity = (event as OrderCreated).getOrder().toJpa()
        orderRepository.save(orderEntity)
    }

    private fun OrderState.toJpa(): OrderEntity {
        return OrderEntity(id.value, customer, status, cost, items.map { it.toJpa() })
    }

    private fun OrderItemState.toJpa(): OrderItemEntity {
        return OrderItemEntity(UUID.randomUUID().toString(), productName, quantity, size, milk)
    }
}

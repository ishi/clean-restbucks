package be.sourcedbvba.restbucks.order.infra.persistence.event

import be.sourcedbvba.restbucks.order.domain.services.event.*
import be.sourcedbvba.restbucks.order.infra.persistence.gateway.jpa.OrderEntity
import be.sourcedbvba.restbucks.order.infra.persistence.gateway.jpa.OrderItemEntity
import java.util.*

val orderCreatedConsumer = { save: (OrderEntity) -> OrderEntity, event: DomainEvent ->
    run {
        if(event is OrderCreated) {
            fun OrderItemState.toJpa(): OrderItemEntity {
                return OrderItemEntity(UUID.randomUUID().toString(), productName, quantity, size, milk)
            }

            fun OrderState.toJpa(): OrderEntity {
                return OrderEntity(id.value, customer, status, cost, items.map { it.toJpa() })
            }

            val orderEntity = event.getOrder().toJpa()
            save(orderEntity)
        }
    }
}

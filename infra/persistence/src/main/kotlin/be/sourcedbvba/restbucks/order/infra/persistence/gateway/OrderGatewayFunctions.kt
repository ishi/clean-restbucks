package be.sourcedbvba.restbucks.order.infra.persistence.gateway

import be.sourcedbvba.restbucks.order.domain.model.Order
import be.sourcedbvba.restbucks.order.domain.model.OrderId
import be.sourcedbvba.restbucks.order.domain.model.OrderItem
import be.sourcedbvba.restbucks.order.domain.model.OrderItems
import be.sourcedbvba.restbucks.order.infra.persistence.gateway.jpa.OrderEntity
import be.sourcedbvba.restbucks.order.infra.persistence.gateway.jpa.OrderItemEntity
import java.util.*

fun OrderItemEntity.toDomain(): OrderItem {
    return OrderItem(product, quantity, size, milk)
}

fun OrderEntity.toDomain(): Order {
    return Order(OrderId(id), customerName, status, OrderItems(items.map { it.toDomain() }))
}

val getOrder = { findById: (String) -> Optional<OrderEntity>, orderId: OrderId ->
    run {
        findById(orderId.value).map { it.toDomain() }.orElseThrow { IllegalArgumentException() }
    }
}

val getOrders = { findAll: () -> List<OrderEntity> ->
    run {
        findAll().map { it.toDomain() }
    }
}

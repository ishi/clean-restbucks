package be.sourcedbvba.restbucks.order.infra.persistence.gateway

import be.sourcedbvba.restbucks.order.domain.model.Order
import be.sourcedbvba.restbucks.order.infra.persistence.gateway.jpa.OrderEntity
import be.sourcedbvba.restbucks.order.domain.model.OrderId
import be.sourcedbvba.restbucks.order.domain.model.OrderItem
import be.sourcedbvba.restbucks.order.infra.persistence.gateway.jpa.OrderItemEntity
import be.sourcedbvba.restbucks.order.domain.model.OrderItems
import be.sourcedbvba.restbucks.order.domain.services.gateway.OrderGateway
import be.sourcedbvba.restbucks.order.infra.persistence.gateway.jpa.OrderRepository
import java.lang.IllegalArgumentException

class OrderGatewayImpl constructor(private val orderRepository: OrderRepository) : OrderGateway {
    override fun getOrder(orderId: OrderId): Order {
        return orderRepository.findById(orderId.value).map { it.toDomain() }.orElseThrow { IllegalArgumentException() }
    }

    override fun getOrders(): List<Order> {
        return orderRepository.findAll().map { it.toDomain() }
    }

    private fun OrderEntity.toDomain(): Order {
        return Order(OrderId(id), customerName, status, OrderItems(items.map { it.toDomain() }))
    }

    private fun OrderItemEntity.toDomain(): OrderItem {
        return OrderItem(product, quantity, size, milk)
    }
}

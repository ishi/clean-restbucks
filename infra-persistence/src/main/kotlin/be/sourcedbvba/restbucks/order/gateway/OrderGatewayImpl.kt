package be.sourcedbvba.restbucks.order.gateway

import be.sourcedbvba.restbucks.order.Order
import be.sourcedbvba.restbucks.order.gateway.jpa.OrderEntity
import be.sourcedbvba.restbucks.order.OrderId
import be.sourcedbvba.restbucks.order.OrderItem
import be.sourcedbvba.restbucks.order.gateway.jpa.OrderItemEntity
import be.sourcedbvba.restbucks.order.OrderItems
import be.sourcedbvba.restbucks.order.gateway.jpa.OrderRepository
import java.lang.IllegalArgumentException

class OrderGatewayImpl constructor(private val orderRepository: OrderRepository) : OrderGateway {
    override fun getOrder(orderId: OrderId): Order {
        return orderRepository.findById(orderId.value).map { it.toDomain() }.orElseThrow { IllegalArgumentException() }
    }

    override fun getOrders(): List<Order> {
        return orderRepository.findAll().map { it.toDomain() }
    }

    internal fun OrderEntity.toDomain(): Order {
        return Order(OrderId(id), customerName, status, OrderItems(items.map { it.toDomain() }))
    }

    internal fun OrderItemEntity.toDomain(): OrderItem {
        return OrderItem(product, quantity, size, milk)
    }
}

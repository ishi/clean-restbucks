package be.sourcedbvba.restbucks.order.gateway

import be.sourcedbvba.restbucks.order.*
import org.springframework.stereotype.Component

@Component
internal class OrderGatewayImpl internal constructor(private val orderRepository: OrderRepository) : OrderGateway {
    override fun getOrder(orderId: OrderId): Order {
        return orderRepository.getOne(orderId.value).toDomain()
    }

    override fun getOrders(): List<Order> {
        return orderRepository.findAll().map { it.toDomain() }
    }

    internal fun OrderEntity.toDomain() : Order {
        return Order(OrderId(id), customerName, status, OrderItems(items.map { it.toDomain() }))
    }

    internal fun OrderItemEntity.toDomain() : OrderItem {
        return OrderItem(product, quantity, size, milk)
    }
}

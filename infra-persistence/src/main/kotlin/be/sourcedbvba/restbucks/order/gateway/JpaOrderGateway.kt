package be.sourcedbvba.restbucks.order.gateway

import be.sourcedbvba.restbucks.order.*
import org.springframework.stereotype.Component

@Component
internal class JpaOrderGateway internal constructor(private val orderJpaRepository: OrderJpaRepository) : OrderGateway {
    override fun getOrder(orderId: OrderId): Order {
        return orderJpaRepository.getOne(orderId.value).toDomain()
    }

    override fun getOrders(): List<Order> {
        return orderJpaRepository.findAll().map { it.toDomain() }
    }

    internal fun OrderEntity.toDomain() : Order {
        return Order(OrderId(id), customerName, status, items.map { it.toDomain() })
    }

    internal fun OrderItemEntity.toDomain() : OrderItem {
        return OrderItem(product, quantity, size, milk)
    }
}
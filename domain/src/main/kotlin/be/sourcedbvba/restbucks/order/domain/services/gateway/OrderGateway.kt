package be.sourcedbvba.restbucks.order.domain.services.gateway

import be.sourcedbvba.restbucks.order.domain.model.Order
import be.sourcedbvba.restbucks.order.domain.model.OrderId

interface OrderGateway {
    fun getOrders(): List<Order>
    fun getOrder(orderId: OrderId): Order
}

package be.sourcedbvba.restbucks.order.gateway

import be.sourcedbvba.restbucks.order.Order
import be.sourcedbvba.restbucks.order.OrderId

interface OrderGateway {
    fun getOrders() : List<Order>
    fun getOrder(orderId: OrderId) : Order
}
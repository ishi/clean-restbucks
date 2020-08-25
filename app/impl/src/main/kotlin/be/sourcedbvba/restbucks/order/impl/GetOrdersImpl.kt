package be.sourcedbvba.restbucks.order.impl

import be.sourcedbvba.restbucks.order.api.*
import be.sourcedbvba.restbucks.order.domain.model.Order
import be.sourcedbvba.restbucks.order.domain.model.OrderItem
import be.sourcedbvba.restbucks.order.domain.model.OrderItems

val getOrdersUseCase = { getOrders: () -> List<Order> ->
    fun OrderItem.toResponse() = GetOrdersResponseItem(productName, quantity, size, milk)

    fun OrderItems.toResponses(): List<GetOrdersResponseItem> {
        return map { item ->
            item.toResponse()
        }
    }

    fun Order.toResponse(): GetOrdersResponse {
        return GetOrdersResponse(id.value, customer, status, items.toResponses())
    }

    {
        val orders = getOrders()
        orders.map { it.toResponse() }
    }
}

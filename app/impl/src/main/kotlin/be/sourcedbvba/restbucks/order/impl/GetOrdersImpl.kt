package be.sourcedbvba.restbucks.order.impl

import be.sourcedbvba.restbucks.order.api.*
import be.sourcedbvba.restbucks.order.domain.model.Order
import be.sourcedbvba.restbucks.order.domain.model.OrderItem
import be.sourcedbvba.restbucks.order.domain.model.OrderItems

val getOrdersUseCase = { getOrders: () -> List<Order> -> run {
        val orders = getOrders()
        orders.map { it.toResponse() }
    }
}

private fun OrderItem.toResponse() = GetOrdersResponseItem(productName, quantity, size, milk)

private fun OrderItems.toResponses(): List<GetOrdersResponseItem> {
    return map { item ->
        item.toResponse()
    }
}

private fun Order.toResponse(): GetOrdersResponse {
    return GetOrdersResponse(id.value, customer, status, items.toResponses())
}

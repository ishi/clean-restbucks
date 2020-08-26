package be.sourcedbvba.restbucks.order.impl

import be.sourcedbvba.restbucks.order.api.CreateOrderRequest
import be.sourcedbvba.restbucks.order.api.CreateOrderRequestItem
import be.sourcedbvba.restbucks.order.api.CreateOrderResponse
import be.sourcedbvba.restbucks.order.domain.model.Order
import be.sourcedbvba.restbucks.order.domain.model.OrderId
import be.sourcedbvba.restbucks.order.domain.model.OrderItem
import be.sourcedbvba.restbucks.order.domain.model.OrderItems
import be.sourcedbvba.restbucks.order.shared.vocabulary.Status
import java.util.*

val createOrderUseCase = { request: CreateOrderRequest ->
    run {
        fun CreateOrderRequestItem.toOrderItem(): OrderItem {
            return OrderItem(product, quantity, size, milk)
        }

        fun CreateOrderRequest.toOrder(): Order {
            val id = UUID.randomUUID().toString()
            return Order(OrderId(id), customer, Status.OPEN, OrderItems(items.map { it.toOrderItem() }))
        }

        fun Order.toResponse(): CreateOrderResponse {
            return CreateOrderResponse(id.value, customer, cost)
        }

        val order = request.toOrder()
        order.create()
        order.toResponse()
    }
}


package be.sourcedbvba.restbucks.order.impl

import be.sourcedbvba.restbucks.order.api.*
import be.sourcedbvba.restbucks.order.domain.model.OrderId
import be.sourcedbvba.restbucks.order.domain.model.Order

val getOrderStatusUseCase = { getOrder: (OrderId) -> Order, request: GetOrderStatusRequest ->
    run {
        val order = getOrder(OrderId(request.orderId))
        GetOrderStatusResponse(order.status)
    }
}

package be.sourcedbvba.restbucks.order.impl

import be.sourcedbvba.restbucks.order.api.DeliverOrderRequest
import be.sourcedbvba.restbucks.order.domain.model.Order
import be.sourcedbvba.restbucks.order.domain.model.OrderId

val deliverOrderUseCase = { getOrder: (OrderId) -> Order, request: DeliverOrderRequest ->
    run {
        val order = getOrder(OrderId(request.orderId))
        order.deliver()
    }
}

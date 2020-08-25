package be.sourcedbvba.restbucks.order.impl

import be.sourcedbvba.restbucks.order.api.PayOrderRequest
import be.sourcedbvba.restbucks.order.domain.model.OrderId
import be.sourcedbvba.restbucks.order.domain.model.Order

val payOrderUseCase = { getOrder: (OrderId) -> Order, request: PayOrderRequest ->
    run {
        val order = getOrder(OrderId(request.orderId))
        order.pay()
    }
}

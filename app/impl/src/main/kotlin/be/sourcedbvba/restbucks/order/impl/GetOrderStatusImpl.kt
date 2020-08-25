package be.sourcedbvba.restbucks.order.impl

import be.sourcedbvba.restbucks.order.api.*
import be.sourcedbvba.restbucks.order.domain.model.OrderId
import be.sourcedbvba.restbucks.order.domain.model.Order
import be.sourcedbvba.restbucks.order.domain.services.gateway.OrderGateway
import be.sourcedbvba.restbucks.order.shared.vocabulary.UseCase

val getOrderStatusUseCase = { getOrder: (OrderId) -> Order, request: GetOrderStatusRequest ->
    run {
        val order = getOrder(OrderId(request.orderId))
        GetOrderStatusResponse(order.status)
    }
}

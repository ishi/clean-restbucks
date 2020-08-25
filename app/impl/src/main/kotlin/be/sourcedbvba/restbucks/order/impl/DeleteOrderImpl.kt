package be.sourcedbvba.restbucks.order.impl

import be.sourcedbvba.restbucks.order.domain.model.OrderId
import be.sourcedbvba.restbucks.order.api.DeleteOrder
import be.sourcedbvba.restbucks.order.api.DeleteOrderRequest
import be.sourcedbvba.restbucks.order.domain.model.Order
import be.sourcedbvba.restbucks.order.domain.services.gateway.OrderGateway
import be.sourcedbvba.restbucks.order.shared.vocabulary.UseCase

val deleteOrderUseCase = { getOrder: (OrderId) -> Order, request: DeleteOrderRequest ->
    run {
        val order = getOrder(OrderId(request.orderId))
        order.delete()
    }
}

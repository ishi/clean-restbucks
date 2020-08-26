package be.sourcedbvba.restbucks.order.impl

import be.sourcedbvba.restbucks.order.domain.model.OrderId
import be.sourcedbvba.restbucks.order.api.DeleteOrderRequest
import be.sourcedbvba.restbucks.order.domain.model.Order


val deleteOrderUseCase = { getOrder: (OrderId) -> Order, request: DeleteOrderRequest ->
    run {
        val order = getOrder(OrderId(request.orderId))
        order.delete()
    }
}

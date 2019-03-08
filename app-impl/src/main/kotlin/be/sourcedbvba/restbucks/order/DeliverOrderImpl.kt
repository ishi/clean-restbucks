package be.sourcedbvba.restbucks.order

import be.sourcedbvba.restbucks.order.gateway.OrderGateway
import be.sourcedbvba.restbucks.usecase.UseCase

@UseCase
internal class DeliverOrderImpl(val orderGateway: OrderGateway) : DeliverOrder {
    override fun deliver(request: DeliverOrderRequest) {
        val order = orderGateway.getOrder(OrderId(request.orderId))
        order.deliver()
    }
}

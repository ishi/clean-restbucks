package be.sourcedbvba.restbucks.order.impl

import be.sourcedbvba.restbucks.order.domain.model.OrderId
import be.sourcedbvba.restbucks.order.api.DeliverOrder
import be.sourcedbvba.restbucks.order.api.DeliverOrderRequest
import be.sourcedbvba.restbucks.order.domain.services.gateway.OrderGateway
import be.sourcedbvba.restbucks.order.shared.vocabulary.UseCase

@UseCase
internal class DeliverOrderImpl(val orderGateway: OrderGateway) : DeliverOrder {
    override fun deliver(request: DeliverOrderRequest) {
        val order = orderGateway.getOrder(OrderId(request.orderId))
        order.deliver()
    }
}

package be.sourcedbvba.restbucks.order.impl

import be.sourcedbvba.restbucks.order.domain.model.OrderId
import be.sourcedbvba.restbucks.order.api.PayOrder
import be.sourcedbvba.restbucks.order.api.PayOrderRequest
import be.sourcedbvba.restbucks.order.domain.services.gateway.OrderGateway
import be.sourcedbvba.restbucks.order.shared.vocabulary.UseCase

@UseCase
internal class PayOrderImpl(private val orderGateway: OrderGateway) : PayOrder {
    override fun pay(request: PayOrderRequest) {
        val order = orderGateway.getOrder(OrderId(request.orderId))
        order.pay()
    }
}

package be.sourcedbvba.restbucks.order

import be.sourcedbvba.restbucks.order.gateway.OrderGateway
import be.sourcedbvba.restbucks.usecase.UseCase

@UseCase
internal class PayOrderImpl(private val orderGateway: OrderGateway) : PayOrder {
    override fun pay(request: PayOrderRequest) {
        val order = orderGateway.getOrder(OrderId(request.orderId))
        order.pay()
    }
}

package be.sourcedbvba.restbucks.order

import be.sourcedbvba.restbucks.order.gateway.OrderGateway
import be.sourcedbvba.restbucks.usecase.UseCase

@UseCase
internal class GetOrderStatusImpl(val orderGateway: OrderGateway) : GetOrderStatus {
    override fun getStatus(request: GetOrderStatusRequest, presenter: GetOrderStatusReceiver) {
        val order = orderGateway.getOrder(request.orderId)
        presenter.receive(GetOrderStatusResponse(order.status))
    }
}
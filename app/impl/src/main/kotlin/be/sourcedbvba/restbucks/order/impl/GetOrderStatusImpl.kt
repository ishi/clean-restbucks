package be.sourcedbvba.restbucks.order.impl

import be.sourcedbvba.restbucks.order.domain.model.OrderId
import be.sourcedbvba.restbucks.order.api.GetOrderStatus
import be.sourcedbvba.restbucks.order.api.GetOrderStatusReceiver
import be.sourcedbvba.restbucks.order.api.GetOrderStatusRequest
import be.sourcedbvba.restbucks.order.api.GetOrderStatusResponse
import be.sourcedbvba.restbucks.order.domain.services.gateway.OrderGateway
import be.sourcedbvba.restbucks.order.shared.vocabulary.UseCase

@UseCase(transactional = false)
internal class GetOrderStatusImpl(val orderGateway: OrderGateway) : GetOrderStatus {
    override fun getStatus(request: GetOrderStatusRequest, presenter: GetOrderStatusReceiver) {
        val order = orderGateway.getOrder(OrderId(request.orderId))
        presenter.receive(GetOrderStatusResponse(order.status))
    }
}

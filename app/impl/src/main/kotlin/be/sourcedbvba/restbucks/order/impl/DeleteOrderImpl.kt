package be.sourcedbvba.restbucks.order.impl

import be.sourcedbvba.restbucks.order.domain.model.OrderId
import be.sourcedbvba.restbucks.order.api.DeleteOrder
import be.sourcedbvba.restbucks.order.api.DeleteOrderRequest
import be.sourcedbvba.restbucks.order.domain.services.gateway.OrderGateway
import be.sourcedbvba.restbucks.order.shared.vocabulary.UseCase

@UseCase
internal class DeleteOrderImpl(
    private val orderGateway: OrderGateway
) : DeleteOrder {
    override fun delete(request: DeleteOrderRequest) {
        val order = orderGateway.getOrder(OrderId(request.orderId))
        order.delete()
    }
}

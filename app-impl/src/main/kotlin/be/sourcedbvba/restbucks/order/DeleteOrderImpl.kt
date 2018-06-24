package be.sourcedbvba.restbucks.order

import be.sourcedbvba.restbucks.order.gateway.OrderGateway
import be.sourcedbvba.restbucks.usecase.UseCase

@UseCase
internal class DeleteOrderImpl(private val orderGateway: OrderGateway) : DeleteOrder {
    override fun delete(request: DeleteOrderRequest) {
        val order = orderGateway.getOrder(OrderId(request.orderId))
        order.delete()
    }
}
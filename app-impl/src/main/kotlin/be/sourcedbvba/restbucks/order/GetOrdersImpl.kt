package be.sourcedbvba.restbucks.order

import be.sourcedbvba.restbucks.order.gateway.OrderGateway
import be.sourcedbvba.restbucks.usecase.UseCase
import reactor.core.publisher.Flux

@UseCase
internal class GetOrdersImpl(val orderGateway: OrderGateway) : GetOrders {
    override fun getOrders(presenter: GetOrdersReceiver) {
        presenter.receive(orderGateway.getOrders().map { it.toResponse() })
    }

    private fun Order.toResponse() : GetOrdersResponse {
        return GetOrdersResponse(id, customer, status, items.map { it.toResponse() })
    }

    private fun OrderItem.toResponse() : GetOrdersResponseItem {
        return GetOrdersResponseItem(product, quantity, size, milk)
    }
}
package be.sourcedbvba.restbucks.order

import be.sourcedbvba.restbucks.order.gateway.OrderGateway
import be.sourcedbvba.restbucks.usecase.UseCase

@UseCase(transactional = false)
internal class GetOrdersImpl(val orderGateway: OrderGateway) : GetOrders {
    override fun getOrders(presenter: GetOrdersReceiver) {
        val orders = orderGateway.getOrders()
        presenter.receive(orders.map { it.toResponse() })
    }

    private fun Order.toResponse(): GetOrdersResponse {
        return GetOrdersResponse(id.value, customer, status, items.toResponses())
    }

    private fun OrderItems.toResponses(): List<GetOrdersResponseItem> {
        return map { item ->
            item.toResponse()
        }
    }

    private fun OrderItem.toResponse() = GetOrdersResponseItem(productName, quantity, size, milk)
}

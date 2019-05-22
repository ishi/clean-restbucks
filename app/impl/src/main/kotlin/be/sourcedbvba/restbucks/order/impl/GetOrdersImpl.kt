package be.sourcedbvba.restbucks.order.impl

import be.sourcedbvba.restbucks.order.domain.model.Order
import be.sourcedbvba.restbucks.order.domain.model.OrderItem
import be.sourcedbvba.restbucks.order.domain.model.OrderItems
import be.sourcedbvba.restbucks.order.api.GetOrders
import be.sourcedbvba.restbucks.order.api.GetOrdersReceiver
import be.sourcedbvba.restbucks.order.api.GetOrdersResponse
import be.sourcedbvba.restbucks.order.api.GetOrdersResponseItem
import be.sourcedbvba.restbucks.order.domain.services.gateway.OrderGateway
import be.sourcedbvba.restbucks.order.shared.vocabulary.UseCase

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

package be.sourcedbvba.restbucks.order

import be.sourcedbvba.restbucks.Status
import be.sourcedbvba.restbucks.usecase.UseCase
import java.util.*

@UseCase
internal class CreateOrderImpl : CreateOrder {
    override fun create(request: CreateOrderRequest, presenter: CreateOrderReceiver) {
        val order = request.toOrder()
        order.create()
        presenter.receive(order.toResponse())
    }

    private fun CreateOrderRequest.toOrder() : Order {
        val id = UUID.randomUUID().toString()
        return Order(id, customer, Status.OPEN, items.map { it.toOrderItem() })
    }

    private fun CreateOrderRequestItem.toOrderItem(): OrderItem {
        return OrderItem(product, quantity, size, milk)
    }

    private fun Order.toResponse() : CreateOrderResponse {
        return CreateOrderResponse(id, customer, cost)
    }
}
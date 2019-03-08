package be.sourcedbvba.restbucks.order

import be.sourcedbvba.restbucks.Status
import be.sourcedbvba.restbucks.usecase.UseCase
import java.util.UUID

@UseCase
internal class CreateOrderImpl : CreateOrder {
    override fun create(request: CreateOrderRequest, presenter: CreateOrderReceiver) {
        val order = request.toOrder()
        order.create()
        presenter.receive(order.toResponse())
    }

    private fun CreateOrderRequest.toOrder(): Order {
        val id = UUID.randomUUID().toString()
        return Order(OrderId(id), customer, Status.OPEN, OrderItems(items.map { it.toOrderItem() }))
    }

    private fun CreateOrderRequestItem.toOrderItem(): OrderItem {
        return OrderItem(product, quantity, size, milk)
    }

    private fun Order.toResponse(): CreateOrderResponse {
        return CreateOrderResponse(id.value, customer, cost)
    }
}

package be.sourcedbvba.restbucks.order

import be.sourcedbvba.restbucks.Status
import be.sourcedbvba.restbucks.order.gateway.OrderGateway
import be.sourcedbvba.restbucks.usecase.UseCase
import reactor.core.publisher.Mono
import java.util.*

@UseCase
internal class CreateOrderImpl(val orderGateway: OrderGateway) : CreateOrder {
    override fun create(request: Mono<CreateOrderRequest>, presenter: CreateOrderReceiver) {
        presenter.receive(request.flatMap {
            val order = it.toOrder()
            order.create();
            orderGateway.getOrder(order.id).map { it.toResponse() }
        })
    }

    private fun CreateOrderRequest.toOrder(): Order {
        val id = UUID.randomUUID().toString()
        return Order(id, customer, Status.OPEN, items.map { it.toOrderItem() })
    }

    private fun CreateOrderRequestItem.toOrderItem(): OrderItem {
        return OrderItem(product, quantity, size, milk)
    }

    private fun Order.toResponse(): CreateOrderResponse {
        return CreateOrderResponse(id, customer, cost)
    }
}
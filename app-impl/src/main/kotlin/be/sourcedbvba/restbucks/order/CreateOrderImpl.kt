package be.sourcedbvba.restbucks.order

import be.sourcedbvba.restbucks.Status
import be.sourcedbvba.restbucks.domain.transaction.TransactionFactory
import be.sourcedbvba.restbucks.usecase.UseCase
import java.util.*
import java.util.function.Supplier

@UseCase
internal class CreateOrderImpl(private val transactionFactory: TransactionFactory) : CreateOrder {
    override fun create(request: CreateOrderRequest, presenter: CreateOrderReceiver) {
        request.validate()
        val transaction = transactionFactory.start()
        try {
            val order = request.toOrder()
            order.create()
            presenter.receive(order.toResponse())
            transaction.commit()
        } catch(ex: Exception) {
            transaction.rollback()
        }
    }

    private fun CreateOrderRequest.validate() {

    }

    private fun CreateOrderRequest.toOrder() : Order {
        val id = UUID.randomUUID().toString()
        return Order(OrderId(id), customer, Status.OPEN, OrderItems(items.map { it.toOrderItem() }))
    }

    private fun CreateOrderRequestItem.toOrderItem(): OrderItem {
        return OrderItem(product, quantity, size, milk)
    }

    private fun Order.toResponse() : CreateOrderResponse {
        return CreateOrderResponse(id.value, customer, cost)
    }
}
package be.sourcedbvba.restbucks.order.domain.services.event

import be.sourcedbvba.restbucks.order.shared.vocabulary.Milk
import be.sourcedbvba.restbucks.order.shared.vocabulary.Size
import be.sourcedbvba.restbucks.order.shared.vocabulary.Status
import be.sourcedbvba.restbucks.order.domain.model.Order
import be.sourcedbvba.restbucks.order.domain.model.OrderId
import java.math.BigDecimal

interface OrderCreated : DomainEvent {
    fun getOrder(): OrderState
}

internal data class OrderCreatedEvent(private val order: Order) : OrderCreated {
    override fun getOrder(): OrderState {
        return OrderState(order.id, order.customer, order.status, order.cost, order.items.map {
            OrderItemState(it.productName, it.quantity, it.size, it.milk)
        })
    }
}

data class OrderState(
        val id: OrderId,
        val customer: String,
        val status: Status,
        val cost: BigDecimal,
        val items: List<OrderItemState>
)

data class OrderItemState(
        val productName: String,
        val quantity: Int,
        val size: Size,
        val milk: Milk
)

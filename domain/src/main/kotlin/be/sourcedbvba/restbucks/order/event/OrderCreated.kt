package be.sourcedbvba.restbucks.order.event

import be.sourcedbvba.restbucks.Milk
import be.sourcedbvba.restbucks.Size
import be.sourcedbvba.restbucks.Status
import be.sourcedbvba.restbucks.domain.event.DomainEvent
import be.sourcedbvba.restbucks.order.Order
import be.sourcedbvba.restbucks.order.OrderId
import java.math.BigDecimal

interface OrderCreated : DomainEvent {
    fun getOrder() : OrderState
}

internal data class OrderCreatedEvent(private val order: Order) : OrderCreated {
    override fun getOrder(): OrderState {
        return OrderState(order.id, order.customer, order.status, order.cost, order.items.map {
            OrderItemState(it.productName, it.quantity, it.size, it.milk)
        })

    }
}

data class OrderState(val id: OrderId,
                      val customer: String,
                      val status: Status,
                      val cost: BigDecimal,
                      val items: List<OrderItemState>)

data class OrderItemState(val productName: String,
                          val quantity: Int,
                          val size: Size,
                          val milk: Milk)
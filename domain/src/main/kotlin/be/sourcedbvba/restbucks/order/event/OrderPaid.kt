package be.sourcedbvba.restbucks.order.event

import be.sourcedbvba.restbucks.domain.event.DomainEvent
import be.sourcedbvba.restbucks.order.OrderId

interface OrderPaid : DomainEvent {
    fun getId() : OrderId
}

internal data class OrderPaidEvent(private val id: OrderId) : OrderPaid {
    override fun getId(): OrderId {
        return id
    }
}
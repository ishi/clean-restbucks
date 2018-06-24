package be.sourcedbvba.restbucks.order.event

import be.sourcedbvba.restbucks.domain.event.DomainEvent
import be.sourcedbvba.restbucks.order.OrderId

interface OrderDelivered : DomainEvent {
    fun getId() : OrderId
}

internal data class OrderDeliveredEvent(private val id: OrderId) : OrderDelivered {
    override fun getId(): OrderId {
        return id
    }
}
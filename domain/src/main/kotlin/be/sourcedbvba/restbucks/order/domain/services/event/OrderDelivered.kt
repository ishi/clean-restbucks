package be.sourcedbvba.restbucks.order.domain.services.event

import be.sourcedbvba.restbucks.order.domain.model.OrderId

interface OrderDelivered : DomainEvent {
    fun getId(): OrderId
}

internal data class OrderDeliveredEvent(private val id: OrderId) : OrderDelivered {
    override fun getId(): OrderId {
        return id
    }
}

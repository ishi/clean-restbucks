package be.sourcedbvba.restbucks.order.domain.services.event

import be.sourcedbvba.restbucks.order.domain.model.OrderId

interface OrderPaid : DomainEvent {
    fun getId(): OrderId
}

internal data class OrderPaidEvent(private val id: OrderId) : OrderPaid {
    override fun getId(): OrderId {
        return id
    }
}

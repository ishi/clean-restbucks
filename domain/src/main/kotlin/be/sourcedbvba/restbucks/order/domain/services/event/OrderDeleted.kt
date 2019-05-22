package be.sourcedbvba.restbucks.order.domain.services.event

import be.sourcedbvba.restbucks.order.domain.model.OrderId

interface OrderDeleted : DomainEvent {
    fun getId(): OrderId
}

internal data class OrderDeletedEvent(private val id: OrderId) : OrderDeleted {
    override fun getId(): OrderId {
        return id
    }
}

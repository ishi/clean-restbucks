package be.sourcedbvba.restbucks.order.event

import be.sourcedbvba.restbucks.domain.event.DomainEvent
import be.sourcedbvba.restbucks.order.OrderId

interface OrderDeleted : DomainEvent {
    fun getId() : OrderId;
}

internal data class OrderDeletedEvent(private val id: OrderId) : OrderDeleted {
    override fun getId(): OrderId {
        return id
    }
}
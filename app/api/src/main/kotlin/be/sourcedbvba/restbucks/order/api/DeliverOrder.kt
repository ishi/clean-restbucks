package be.sourcedbvba.restbucks.order.api

interface DeliverOrder {
    fun deliver(request: DeliverOrderRequest)
}

data class DeliverOrderRequest(val orderId: String)

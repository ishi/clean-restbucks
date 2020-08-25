package be.sourcedbvba.restbucks.order.api

typealias DeliverOrder = (DeliverOrderRequest) -> Unit

data class DeliverOrderRequest(val orderId: String)

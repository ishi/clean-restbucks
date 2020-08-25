package be.sourcedbvba.restbucks.order.api

typealias DeleteOrder = (DeleteOrderRequest) -> Unit

data class DeleteOrderRequest(val orderId: String)

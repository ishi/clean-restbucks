package be.sourcedbvba.restbucks.order.api

typealias PayOrder = (PayOrderRequest) -> Unit

data class PayOrderRequest(val orderId: String)

package be.sourcedbvba.restbucks.order.api

interface PayOrder {
    fun pay(request: PayOrderRequest)
}

data class PayOrderRequest(val orderId: String)

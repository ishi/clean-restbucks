package be.sourcedbvba.restbucks.order.api

import be.sourcedbvba.restbucks.order.shared.vocabulary.Milk
import be.sourcedbvba.restbucks.order.shared.vocabulary.Size
import java.math.BigDecimal

interface CreateOrder {
    fun create(request: CreateOrderRequest, presenter: CreateOrderReceiver)
}

data class CreateOrderRequest(val customer: String, val items: List<CreateOrderRequestItem>)
data class CreateOrderRequestItem(val product: String, val quantity: Int, val size: Size, val milk: Milk)
data class CreateOrderResponse(val id: String, val customer: String, val amount: BigDecimal)

interface CreateOrderReceiver {
    fun receive(response: CreateOrderResponse)
}

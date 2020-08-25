package be.sourcedbvba.restbucks.order.api

import be.sourcedbvba.restbucks.order.shared.vocabulary.Milk
import be.sourcedbvba.restbucks.order.shared.vocabulary.Size
import java.math.BigDecimal

typealias CreateOrder = (CreateOrderRequest) -> CreateOrderResponse

data class CreateOrderRequest(val customer: String, val items: List<CreateOrderRequestItem>)
data class CreateOrderRequestItem(val product: String, val quantity: Int, val size: Size, val milk: Milk)
data class CreateOrderResponse(val id: String, val customer: String, val amount: BigDecimal)

package be.sourcedbvba.restbucks.order.api

import be.sourcedbvba.restbucks.order.shared.vocabulary.Milk
import be.sourcedbvba.restbucks.order.shared.vocabulary.Size
import be.sourcedbvba.restbucks.order.shared.vocabulary.Status

typealias GetOrders = () -> List<GetOrdersResponse>

data class GetOrdersResponse(
        val id: String,
        val customer: String,
        val status: Status,
        val items: List<GetOrdersResponseItem>
)
data class GetOrdersResponseItem(
        val product: String,
        val quantity: Int,
        val size: Size,
        val milk: Milk
)

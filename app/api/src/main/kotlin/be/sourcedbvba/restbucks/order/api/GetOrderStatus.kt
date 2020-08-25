package be.sourcedbvba.restbucks.order.api

import be.sourcedbvba.restbucks.order.shared.vocabulary.Status

typealias GetOrderStatus = (GetOrderStatusRequest) -> GetOrderStatusResponse

data class GetOrderStatusRequest(val orderId: String)
data class GetOrderStatusResponse(val status: Status)


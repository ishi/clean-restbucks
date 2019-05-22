package be.sourcedbvba.restbucks.order.api

import be.sourcedbvba.restbucks.order.shared.vocabulary.Status

interface GetOrderStatus {
    fun getStatus(request: GetOrderStatusRequest, presenter: GetOrderStatusReceiver)
}

data class GetOrderStatusRequest(val orderId: String)
data class GetOrderStatusResponse(val status: Status)

interface GetOrderStatusReceiver {
    fun receive(response: GetOrderStatusResponse)
}

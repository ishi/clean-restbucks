package be.sourcedbvba.restbucks.order

import be.sourcedbvba.restbucks.Status
import reactor.core.publisher.Mono

interface GetOrderStatus {
    fun getStatus(request: GetOrderStatusRequest, presenter: GetOrderStatusReceiver)
}

data class GetOrderStatusRequest(val orderId: String)
data class GetOrderStatusResponse(val status: Status)

interface GetOrderStatusReceiver {
    fun receive(response: Mono<GetOrderStatusResponse>)
}

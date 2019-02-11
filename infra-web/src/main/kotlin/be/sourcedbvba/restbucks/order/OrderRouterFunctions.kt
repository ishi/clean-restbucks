package be.sourcedbvba.restbucks.order

import be.sourcedbvba.restbucks.Status
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.math.BigDecimal
import java.util.stream.Collectors

@Component
class OrderRouterFunctions(val createOrder: CreateOrder,
                           val getOrders: GetOrders,
                           val getOrderStatus: GetOrderStatus,
                           val deleteOrder: DeleteOrder,
                           val deliverOrder: DeliverOrder,
                           val payOrder: PayOrder) {
    fun createOrder(request:ServerRequest): Mono<ServerResponse> {
        val receiver = CreateOrderJsonReceiver()
        return request.bodyToMono(CreateOrderRequest::class.java).flatMap {
            createOrder.create(it, receiver)
            receiver.result
        }
    }

    fun headOrders(request: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok().build()
    }

    fun getOrders(request: ServerRequest): Mono<ServerResponse> {
        val receiver = GetOrdersJsonReceiver()
        getOrders.getOrders(receiver)
        return receiver.result
    }

    fun getOrderStatus(request: ServerRequest): Mono<ServerResponse> {
        val receiver = GetOrderStatusJsonReceiver()
        val orderId = request.pathVariable("id")
        getOrderStatus.getStatus(GetOrderStatusRequest(orderId), receiver)
        return receiver.result
    }

    fun payForOrder(request: ServerRequest): Mono<ServerResponse> {
        val orderId = request.pathVariable("id")
        payOrder.pay(PayOrderRequest(orderId))
        return ServerResponse.ok().build()
    }

    fun deliverOrder(request: ServerRequest): Mono<ServerResponse> {
        val orderId = request.pathVariable("id")
        deliverOrder.deliver(DeliverOrderRequest(orderId))
        return ServerResponse.ok().build()
    }

    fun deleteOrder(request: ServerRequest): Mono<ServerResponse> {
        val orderId = request.pathVariable("id")
        deleteOrder.delete(DeleteOrderRequest(orderId))
        return ServerResponse.ok().build()
    }

    class CreateOrderJsonReceiver : CreateOrderReceiver {
        internal lateinit var result : Mono<ServerResponse>
            private set

        override fun receive(response: CreateOrderResponse) {
            result = ServerResponse
                    .ok()
                    .body(Mono.just(response.toResponseBody()), CreateOrderResponseBody::class.java)
        }

        internal data class CreateOrderResponseBody(val id: String, val customer: String, val amount: BigDecimal, val _links: Map<String, HalLink>)
        data class HalLink(val href: String)

        private fun CreateOrderResponse.toResponseBody(): CreateOrderResponseBody {
            val links = mapOf(Pair("status", HalLink("/$id/status")))
            return CreateOrderResponseBody(id, customer, amount, links)
        }
    }


    class GetOrdersJsonReceiver : GetOrdersReceiver {
        lateinit var result : Mono<ServerResponse>
            private set

        override fun receive(response: GetOrdersResponses) {
            val mapped: List<GetOrdersResponseBody> = response.stream().map { it.toResponseBody() }.collect(Collectors.toList())
            result = ServerResponse
                    .ok()
                    .body(Flux.fromIterable(mapped), GetOrdersResponseBody::class.java)
        }

        internal data class GetOrdersResponseBody(val id: String, val customer: String, val status: String)

        private fun GetOrdersResponse.toResponseBody() : GetOrdersResponseBody {
            return GetOrdersResponseBody(id, customer, status.name.toLowerCase())
        }
    }

    class GetOrderStatusJsonReceiver: GetOrderStatusReceiver {
        lateinit var result : Mono<ServerResponse>
            private set

        override fun receive(response: GetOrderStatusResponse) {
            result = ServerResponse.ok().body(Mono.just(response.status), Status::class.java)
        }
    }
}

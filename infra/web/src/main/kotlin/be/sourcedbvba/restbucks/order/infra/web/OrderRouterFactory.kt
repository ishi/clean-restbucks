package be.sourcedbvba.restbucks.order.infra.web

import be.sourcedbvba.restbucks.order.shared.vocabulary.Status
import be.sourcedbvba.restbucks.order.api.CreateOrder
import be.sourcedbvba.restbucks.order.api.CreateOrderReceiver
import be.sourcedbvba.restbucks.order.api.CreateOrderRequest
import be.sourcedbvba.restbucks.order.api.CreateOrderResponse
import be.sourcedbvba.restbucks.order.api.DeleteOrder
import be.sourcedbvba.restbucks.order.api.DeleteOrderRequest
import be.sourcedbvba.restbucks.order.api.DeliverOrder
import be.sourcedbvba.restbucks.order.api.DeliverOrderRequest
import be.sourcedbvba.restbucks.order.api.GetOrderStatus
import be.sourcedbvba.restbucks.order.api.GetOrderStatusReceiver
import be.sourcedbvba.restbucks.order.api.GetOrderStatusRequest
import be.sourcedbvba.restbucks.order.api.GetOrderStatusResponse
import be.sourcedbvba.restbucks.order.api.GetOrders
import be.sourcedbvba.restbucks.order.api.GetOrdersReceiver
import be.sourcedbvba.restbucks.order.api.GetOrdersResponse
import be.sourcedbvba.restbucks.order.api.GetOrdersResponses
import be.sourcedbvba.restbucks.order.api.PayOrder
import be.sourcedbvba.restbucks.order.api.PayOrderRequest
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.math.BigDecimal
import java.util.stream.Collectors

class OrderRouterFactory(
        private val createOrder: CreateOrder,
        private val getOrders: GetOrders,
        private val getOrderStatus: GetOrderStatus,
        private val deleteOrder: DeleteOrder,
        private val deliverOrder: DeliverOrder,
        private val payOrder: PayOrder
) {

    fun create() = router {
        GET("/").invoke { home() }
        GET("/order").invoke { getOrders(getOrders)}
        HEAD("/order").invoke { headOrders() }
        POST("/order").and(contentType(MediaType.APPLICATION_JSON)).invoke { r -> createOrder(r, createOrder) }
        POST("/order/{id}/payment").invoke { r -> payForOrder(r, payOrder) }
        POST("/order/{id}/delivery").invoke { r -> deliverOrder(r, deliverOrder) }
        GET("/order/{id}/status").invoke { r -> getOrderStatus(r, getOrderStatus) }
        DELETE("/order/{id}").invoke { r -> deleteOrder(r, deleteOrder) }
    }
}

internal fun createOrder(request: ServerRequest, createOrder: CreateOrder): Mono<ServerResponse> {
    data class HalLink(val href: String)
    data class CreateOrderResponseBody(val id: String, val customer: String, val amount: BigDecimal, val _links: Map<String, HalLink>)

    class CreateOrderJsonReceiver : CreateOrderReceiver {
        lateinit var result: Mono<ServerResponse>
            private set

        override fun receive(response: CreateOrderResponse) {
            result = ServerResponse
                    .ok()
                    .body(Mono.just(response.toResponseBody()), CreateOrderResponseBody::class.java)
        }

        private fun CreateOrderResponse.toResponseBody(): CreateOrderResponseBody {
            val links = mapOf(Pair("status", HalLink("/$id/status")))
            return CreateOrderResponseBody(id, customer, amount, links)
        }
    }

    val receiver = CreateOrderJsonReceiver()
    return request.bodyToMono(CreateOrderRequest::class.java).flatMap {
        createOrder.create(it, receiver)
        receiver.result
    }
}

internal fun headOrders(): Mono<ServerResponse> {
    return ServerResponse.ok().build()
}

internal fun home(): Mono<ServerResponse> {
    return ServerResponse.ok().build()
}

internal fun getOrders(getOrders: GetOrders): Mono<ServerResponse> {
    data class GetOrdersResponseBody(val id: String, val customer: String, val status: String)

    class GetOrdersJsonReceiver : GetOrdersReceiver {
        lateinit var result: Mono<ServerResponse>
            private set

        override fun receive(response: GetOrdersResponses) {
            val mapped: List<GetOrdersResponseBody> = response.stream().map { it.toResponseBody() }.collect(Collectors.toList())
            result = ServerResponse
                    .ok()
                    .body(Flux.fromIterable(mapped), GetOrdersResponseBody::class.java)
        }

        private fun GetOrdersResponse.toResponseBody(): GetOrdersResponseBody {
            return GetOrdersResponseBody(id, customer, status.name.toLowerCase())
        }
    }



    val receiver = GetOrdersJsonReceiver()
    getOrders.getOrders(receiver)
    return receiver.result
}

internal fun getOrderStatus(request: ServerRequest, getOrderStatus: GetOrderStatus): Mono<ServerResponse> {
    class GetOrderStatusJsonReceiver : GetOrderStatusReceiver {
        lateinit var result: Mono<ServerResponse>
            private set

        override fun receive(response: GetOrderStatusResponse) {
            result = ServerResponse.ok().body(Mono.just(response.status), Status::class.java)
        }
    }

    val receiver = GetOrderStatusJsonReceiver()
    val orderId = request.pathVariable("id")
    getOrderStatus.getStatus(GetOrderStatusRequest(orderId), receiver)
    return receiver.result
}

internal fun payForOrder(request: ServerRequest, payOrder: PayOrder): Mono<ServerResponse> {
    val orderId = request.pathVariable("id")
    payOrder.pay(PayOrderRequest(orderId))
    return ServerResponse.ok().build()
}

internal fun deliverOrder(request: ServerRequest, deliverOrder: DeliverOrder): Mono<ServerResponse> {
    val orderId = request.pathVariable("id")
    deliverOrder.deliver(DeliverOrderRequest(orderId))
    return ServerResponse.ok().build()
}

internal fun deleteOrder(request: ServerRequest, deleteOrder: DeleteOrder): Mono<ServerResponse> {
    val orderId = request.pathVariable("id")
    deleteOrder.delete(DeleteOrderRequest(orderId))
    return ServerResponse.ok().build()
}

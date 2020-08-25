package be.sourcedbvba.restbucks.order.infra.web

import arrow.syntax.function.curried
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
        GET("/").invoke(homeFn)
        GET("/order").invoke(getOrdersFn.curried()(getOrders))
        HEAD("/order").invoke(headOrdersFn)
        POST("/order").and(contentType(MediaType.APPLICATION_JSON)).invoke(createOrderFn.curried()(createOrder))
        POST("/order/{id}/payment").invoke(payForOrderFn.curried()(payOrder))
        POST("/order/{id}/delivery").invoke(deliverOrderFn.curried()(deliverOrder))
        GET("/order/{id}/status").invoke(getOrderStatusFn.curried()(getOrderStatus))
        DELETE("/order/{id}").invoke(deleteOrderFn.curried()(deleteOrder))
    }
}

data class HalLink(val href: String)
data class CreateOrderResponseBody(val id: String, val customer: String, val amount: BigDecimal, val _links: Map<String, HalLink>)

private val createOrderFn = { createOrder: CreateOrder, request: ServerRequest  ->
    run {
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
        request.bodyToMono(CreateOrderRequest::class.java).flatMap {
            createOrder.create(it, receiver)
            receiver.result
        }
    }
}

private val simpleOkResponseFn = { serverRequest: ServerRequest -> ServerResponse.ok().build() }

private val headOrdersFn = simpleOkResponseFn
private val homeFn = simpleOkResponseFn

data class GetOrdersResponseBody(val id: String, val customer: String, val status: String)

private val getOrdersFn = { getOrders: GetOrders, request: ServerRequest ->
    run {
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
        receiver.result
    }
}

private val getOrderStatusFn = { getOrderStatus: GetOrderStatus, request: ServerRequest ->
    run {
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
        receiver.result
    }
}

private val payForOrderFn = { payOrder: PayOrder, request: ServerRequest ->
    run {
        val orderId = request.pathVariable("id")
        payOrder.pay(PayOrderRequest(orderId))
        ServerResponse.ok().build()
    }
}

private val deliverOrderFn = { deliverOrder: DeliverOrder, request: ServerRequest ->
    run {
        val orderId = request.pathVariable("id")
        deliverOrder.deliver(DeliverOrderRequest(orderId))
        ServerResponse.ok().build()
    }
}

private val deleteOrderFn = { deleteOrder: DeleteOrder, request: ServerRequest ->
    run {
        val orderId = request.pathVariable("id")
        deleteOrder.delete(DeleteOrderRequest(orderId))
        ServerResponse.ok().build()
    }
}

package be.sourcedbvba.restbucks.order.infra.web

import arrow.syntax.function.curried
import be.sourcedbvba.restbucks.order.api.*
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import java.math.BigDecimal
import java.util.stream.Collectors

val createOrderRouter = { createOrder: CreateOrder,
                          getOrders: GetOrders,
                          getOrderStatus: GetOrderStatus,
                          deleteOrder: DeleteOrder,
                          deliverOrder: DeliverOrder,
                          payOrder: PayOrder ->
    {
        router {
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
}

data class HalLink(val href: String)
data class CreateOrderResponseBody(val id: String, val customer: String, val amount: BigDecimal, val _links: Map<String, HalLink>)

private val createOrderFn = { createOrder: CreateOrder, request: ServerRequest ->
    run {
        val response = request.bodyToMono(CreateOrderRequest::class.java).flatMap {
            val response = createOrder(it)
            val links = mapOf(Pair("status", HalLink("/${response.id}/status")))
            val body = CreateOrderResponseBody(response.id, response.customer, response.amount, links)
            ServerResponse.ok().bodyValue(body)
        }
        response
    }
}

private val simpleOkResponseFn = { serverRequest: ServerRequest -> ServerResponse.ok().build() }

private val headOrdersFn = simpleOkResponseFn
private val homeFn = simpleOkResponseFn

data class GetOrdersResponseBody(val id: String, val customer: String, val status: String)

private val getOrdersFn = { getOrders: GetOrders, request: ServerRequest ->
    run {
        fun GetOrdersResponse.toResponseBody(): GetOrdersResponseBody {
            return GetOrdersResponseBody(id, customer, status.name.toLowerCase())
        }

        val response = getOrders()
        val mapped: List<GetOrdersResponseBody> = response.stream().map { it.toResponseBody() }.collect(Collectors.toList())
        ServerResponse
                .ok()
                .bodyValue(mapped)
    }
}

private val getOrderStatusFn = { getOrderStatus: GetOrderStatus, request: ServerRequest ->
    run {
        val orderId = request.pathVariable("id")
        val response = getOrderStatus(GetOrderStatusRequest(orderId))
        ServerResponse.ok().bodyValue(response.status)
    }
}

private val payForOrderFn = { payOrder: PayOrder, request: ServerRequest ->
    run {
        val orderId = request.pathVariable("id")
        payOrder(PayOrderRequest(orderId))
        ServerResponse.ok().build()
    }
}

private val deliverOrderFn = { deliverOrder: DeliverOrder, request: ServerRequest ->
    run {
        val orderId = request.pathVariable("id")
        deliverOrder(DeliverOrderRequest(orderId))
        ServerResponse.ok().build()
    }
}

private val deleteOrderFn = { deleteOrder: DeleteOrder, request: ServerRequest ->
    run {
        val orderId = request.pathVariable("id")
        deleteOrder(DeleteOrderRequest(orderId))
        ServerResponse.ok().build()
    }
}

package be.sourcedbvba.restbucks.order

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.math.BigDecimal

@RequestMapping("/order")
@RestController
internal class OrderResource(val createOrder: CreateOrder,
                    val getOrders: GetOrders,
                    val getOrderStatus: GetOrderStatus,
                    val deleteOrder: DeleteOrder,
                    val deliverOrder: DeliverOrder,
                    val payOrder: PayOrder) {

    @PostMapping(produces = arrayOf("application/hal+json"))
    @ResponseStatus(HttpStatus.CREATED)
    fun createOrder(@RequestBody createOrderRequest: Mono<CreateOrderRequest>) : ResponseEntity<*> {
        val receiver = CreateOrderJsonReceiver()
        createOrder.create(createOrderRequest, receiver)
        return receiver.result
    }

    @GetMapping
    fun getOrders() : ResponseEntity<*> {
        val receiver = GetOrdersJsonReceiver()
        getOrders.getOrders(receiver)
        return receiver.result
    }

    @GetMapping("/{orderId}/status")
    fun getOrderStatus(@PathVariable orderId: String): ResponseEntity<*> {
        val receiver = GetOrderStatusJsonReceiver()
        getOrderStatus.getStatus(GetOrderStatusRequest(orderId), receiver)
        return receiver.result
    }

    @PostMapping("/{orderId}/payment")
    fun payForOrder(@PathVariable orderId: String) {
        payOrder.pay(PayOrderRequest(orderId))
    }


    @DeleteMapping("/{orderId}")
    fun deleteOrder(@PathVariable orderId: String) {
        deleteOrder.delete(DeleteOrderRequest(orderId))
    }

    @PostMapping("/{orderId}/delivery")
    fun deliverOrder(@PathVariable orderId: String) {
        deliverOrder.deliver(DeliverOrderRequest(orderId))
    }
}

class GetOrdersJsonReceiver : GetOrdersReceiver {
    lateinit var result : ResponseEntity<*>
        private set

    override fun receive(response: Flux<GetOrdersResponse>) {
        result = ResponseEntity.ok(response.map { it.toResponseBody() })
    }

    internal data class GetOrdersResponseBody(val id: String, val customer: String, val status: String)

    internal fun GetOrdersResponse.toResponseBody() : GetOrdersResponseBody {
        return GetOrdersResponseBody(id, customer, status.name.toLowerCase())
    }
}

class CreateOrderJsonReceiver : CreateOrderReceiver {
    lateinit var result : ResponseEntity<*>
        private set

    override fun receive(response: Mono<CreateOrderResponse>) {
        result = ResponseEntity.ok(response.map { it.toResponseBody() })
    }

    internal data class CreateOrderResponseBody(val id: String, val customer: String, val amount: BigDecimal, val _links: Map<String, HalLink>)
    data class HalLink(val href: String)

    internal fun CreateOrderResponse.toResponseBody(): CreateOrderResponseBody {
        val links = mapOf(Pair("status", HalLink("/${id}/status")))
        return CreateOrderResponseBody(id, customer, amount, links)
    }
}

class GetOrderStatusJsonReceiver: GetOrderStatusReceiver {
    lateinit var result : ResponseEntity<*>
        private set

    override fun receive(response: Mono<GetOrderStatusResponse>) {
        result = ResponseEntity.ok(response.map { it.status })
    }
}









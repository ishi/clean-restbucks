package be.sourcedbvba.restbucks.main.config

import be.sourcedbvba.restbucks.order.domain.services.event.DomainEvent
import be.sourcedbvba.restbucks.order.domain.services.event.DomainEventConsumer
import be.sourcedbvba.restbucks.order.domain.services.event.EventPublisher
import org.springframework.context.ApplicationListener
import org.springframework.context.PayloadApplicationEvent
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.ApplicationEventMulticaster
import javax.annotation.PostConstruct

@Configuration
class EventConfiguration(val applicationEventMulticaster: ApplicationEventMulticaster,
                         val eventConsumers: List<DomainEventConsumer>) {
    @PostConstruct
    fun eventPublisher() {
       EventPublisher.Locator.eventPublisher = { event ->
           applicationEventMulticaster.multicastEvent(PayloadApplicationEvent(this, event))
       }

       applicationEventMulticaster.addApplicationListener(ApplicationListener { event: PayloadApplicationEvent<*> ->
           if (event.payload is DomainEvent) {
               val payload = event.payload as DomainEvent
               eventConsumers.find { it.canHandle(payload) }?.consume(payload)
           }
        })
    }
}

package be.sourcedbvba.restbucks.config

import be.sourcedbvba.restbucks.domain.event.DomainEvent
import be.sourcedbvba.restbucks.domain.event.DomainEventConsumer
import be.sourcedbvba.restbucks.domain.event.EventPublisher
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

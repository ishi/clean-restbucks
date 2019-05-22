package be.sourcedbvba.restbucks.main.config

import be.sourcedbvba.restbucks.order.infra.persistence.event.OrderCreatedConsumer
import be.sourcedbvba.restbucks.order.infra.persistence.event.OrderDeletedConsumer
import be.sourcedbvba.restbucks.order.infra.persistence.event.OrderDeliveredConsumer
import be.sourcedbvba.restbucks.order.infra.persistence.event.OrderPaidConsumer
import be.sourcedbvba.restbucks.order.infra.persistence.gateway.OrderGatewayImpl
import be.sourcedbvba.restbucks.order.infra.persistence.gateway.jpa.OrderRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory
import org.springframework.orm.jpa.JpaVendorAdapter
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import javax.persistence.EntityManagerFactory

import javax.sql.DataSource

@Configuration
class PersistenceConfiguration {
    @Bean
    fun orderCreatedConsumer(orderRepository: OrderRepository) = OrderCreatedConsumer(orderRepository)

    @Bean
    fun orderDeletedConsumer(orderRepository: OrderRepository) = OrderDeletedConsumer(orderRepository)

    @Bean
    fun orderDeliveredConsumer(orderRepository: OrderRepository) = OrderDeliveredConsumer(orderRepository)

    @Bean
    fun orderPaidConsumer(orderRepository: OrderRepository) = OrderPaidConsumer(orderRepository)

    @Bean
    fun orderGateway(orderRepository: OrderRepository) = OrderGatewayImpl(orderRepository)

    @Bean
    fun entityManagerFactory(
        jpaVendorAdapter: JpaVendorAdapter,
        dataSource: DataSource,
        environment: Environment
    ): LocalContainerEntityManagerFactoryBean {
        val threadPoolTaskExecutor = ThreadPoolTaskExecutor()
        threadPoolTaskExecutor.isDaemon = true
        threadPoolTaskExecutor.afterPropertiesSet()

        val emf = LocalContainerEntityManagerFactoryBean()
        emf.bootstrapExecutor = threadPoolTaskExecutor
        emf.dataSource = dataSource
        emf.jpaVendorAdapter = jpaVendorAdapter
        emf.setPackagesToScan("be.sourcedbvba.restbucks.order.gateway.jpa")
        return emf
    }
}

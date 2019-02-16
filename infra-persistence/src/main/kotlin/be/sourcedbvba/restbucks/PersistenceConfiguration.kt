package be.sourcedbvba.restbucks

import be.sourcedbvba.restbucks.order.OrderEntity
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.data.repository.config.BootstrapMode
import org.springframework.orm.jpa.JpaVendorAdapter
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

import javax.sql.DataSource

@Configuration
@EnableJpaRepositories(bootstrapMode = BootstrapMode.LAZY)
class PersistenceConfiguration {
    @Bean
    fun entityManagerFactory(jpaVendorAdapter: JpaVendorAdapter,
                             dataSource: DataSource, environment: Environment): LocalContainerEntityManagerFactoryBean {
        val threadPoolTaskExecutor = ThreadPoolTaskExecutor()
        threadPoolTaskExecutor.isDaemon = true
        threadPoolTaskExecutor.afterPropertiesSet()

        val emf = LocalContainerEntityManagerFactoryBean()
        emf.bootstrapExecutor = threadPoolTaskExecutor
        emf.dataSource = dataSource
        emf.jpaVendorAdapter = jpaVendorAdapter
        emf.setPackagesToScan(OrderEntity::class.java.getPackage().name)
        return emf
    }
}

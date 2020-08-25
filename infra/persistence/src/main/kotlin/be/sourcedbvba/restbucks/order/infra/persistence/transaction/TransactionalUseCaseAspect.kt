package be.sourcedbvba.restbucks.order.infra.persistence.transaction

import be.sourcedbvba.restbucks.order.shared.vocabulary.UseCase
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.support.DefaultTransactionDefinition
import java.lang.Exception

@Aspect
class TransactionalUseCaseAspect(private val transactionManager: PlatformTransactionManager) {
    @Around("@within(useCase)")
    fun withinUseCase(pjp: ProceedingJoinPoint, useCase: UseCase) {
        if (useCase.transactional) {
            val tx = transactionManager.getTransaction(DefaultTransactionDefinition())
            try {
                pjp.proceed()
                transactionManager.commit(tx)
            } catch (ex: Exception) {
                transactionManager.rollback(tx)
                throw ex
            }
        } else {
            pjp.proceed()
        }
    }
}

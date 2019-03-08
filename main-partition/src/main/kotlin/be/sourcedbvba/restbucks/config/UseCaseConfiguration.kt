package be.sourcedbvba.restbucks.config

import be.sourcedbvba.restbucks.usecase.UseCase
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.FilterType

@Configuration
@ComponentScan(basePackages = ["be.sourcedbvba.restbucks"],
        includeFilters = [ComponentScan.Filter(type = FilterType.ANNOTATION,
        value = [UseCase::class])])
internal class UseCaseConfiguration

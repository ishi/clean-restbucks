package be.sourcedbvba.restbucks

import org.springframework.boot.autoconfigure.SpringBootApplication

import org.springframework.boot.runApplication

@SpringBootApplication
class RestbucksApplication

fun main(args: Array<String>) {
    runApplication<RestbucksApplication>(*args)
}

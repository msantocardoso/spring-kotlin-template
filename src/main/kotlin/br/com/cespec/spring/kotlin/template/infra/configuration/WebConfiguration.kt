package br.com.cespec.spring.kotlin.template.infra.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.core.env.Environment

@Configuration
@Order(Ordered.LOWEST_PRECEDENCE)
private class SwaggerConfiguration(
    private val environment: Environment
)

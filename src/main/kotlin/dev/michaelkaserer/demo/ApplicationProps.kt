package dev.michaelkaserer.demo

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("demo")
data class ApplicationProps(val auth: Auth) {
    data class Auth(
        val headerName: String = ""
    )
}

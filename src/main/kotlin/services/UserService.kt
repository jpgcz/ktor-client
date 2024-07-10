package services

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.kotlinx.serializer.*
import kotlinx.serialization.serializer

val client = HttpClient(CIO) {
    install(JsonFeature) {
        serializer = KotlinxSerializer()
    }
}
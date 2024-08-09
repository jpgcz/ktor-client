package com.example.services

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.kotlinx.serializer.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UserServiceTest {
    private val mockEngine = MockEngine { request ->
        when (request.url.encodedPath) {
            "/user" -> respond(
                content = """[{"id":1,"name":"persona1","age":20,"email":"persona1@gmail.com"}]""",
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )

            else -> error("Error fetching users")
        }
    }

    private val client = HttpClient(mockEngine) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }

    private val userService = UserService(client)

    @Test
    fun `test getUsers returns list of users`() = runBlocking {
        val result = userController.getUsers()
        assertTrue { result.contains("persona1") }
        assertEquals ("[{\"id\":1,\"name\":\"persona1\",\"age\":20,\"email\":\"persona1@gmail.com\"}]", result)
    }
}
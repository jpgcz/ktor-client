package com.example.services

import UserDTO
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class UserServiceTest {
    private val mockEngine = MockEngine { request ->
        when (request.url.encodedPath) {
            "/user" -> when (request.method) {
                HttpMethod.Get -> {
                    respond(
                        content = """[{"id":1,"name":"persona1","age":20,"email":"persona1@gmail.com"}]""",
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    )
                }

                HttpMethod.Post -> {
                    respond(
                        content = """[{"Message": "User Added Successfully"}]""",
                        status = HttpStatusCode.Created,
                        headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    )
                }

                else -> {
                    respond(
                        content = "Method not allowed",
                        status = HttpStatusCode.MethodNotAllowed,
                        headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    )
                }
            }

            "/user/1" -> when (request.method) {
                HttpMethod.Get -> {
                    respond(
                        content = """[{"id":1,"name":"persona1","age":20,"email":"persona1@gmail.com"}]""",
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    )
                }

                HttpMethod.Patch -> {
                    respond(
                        content = """[{"Message": "User Updated Successfully"}]""",
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    )
                }

                HttpMethod.Delete -> {
                    respond(
                        content = """[{"Message": "User Deleted Successfully"}]""",
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    )
                }

                else -> {
                    respond(
                        content = "Method not allowed",
                        status = HttpStatusCode.MethodNotAllowed,
                        headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    )
                }
            }

            else -> error("Error fetching users")
        }
    }

    val client = HttpClient(mockEngine) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }

    @Test
    fun `test getUsers returns list of users`() = runBlocking {
        val result: String = client.get("http://localhost:8080/user").bodyAsText()
        println(result)
        assertEquals(
            expected = """[{"id":1,"name":"persona1","age":20,"email":"persona1@gmail.com"}]""",
            actual = result,
            message = "Test passed"
        )
    }

    @Test
    fun `test getUserById returns a user`() = runBlocking {
        val result: String = client.get("http://localhost:8080/user/1").bodyAsText()
        println(result)
        assertEquals(
            expected = """[{"id":1,"name":"persona1","age":20,"email":"persona1@gmail.com"}]""",
            actual = result,
            message = "Test passed"
        )
    }

    @Test
    fun `test addUser`() = runBlocking {
        val newUser = UserDTO(2, "persona2", 12, "persona2@gmail.com")
        val result: String = client.post("http://localhost:8080/user") {
            contentType(ContentType.Application.Json)
            setBody(newUser)
        }.bodyAsText()

        assertEquals(
            expected = """[{"Message": "User Added Successfully"}]""",
            actual = result,
            message = "Test passed"
        )
    }

    @Test
    fun `test updateUser`() = runBlocking {
        val newUser = UserDTO(1, "persona1", 20, "persona3@gmail.com")
        val result: String = client.patch("http://localhost:8080/user/1") {
            contentType(ContentType.Application.Json)
            setBody(newUser)
        }.bodyAsText()

        assertEquals(
            expected = """[{"Message": "User Updated Successfully"}]""",
            actual = result,
            message = "Test passed"
        )
    }

    @Test
    fun `test deleteUser`() = runBlocking {
        val result: String = client.delete("http://localhost:8080/user/1").bodyAsText()

        assertEquals(
            expected = """[{"Message": "User Deleted Successfully"}]""",
            actual = result,
            message = "Test passed"
        )
    }
}
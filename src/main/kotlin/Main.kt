import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.lang.Exception
import java.util.Scanner

@Serializable
data class UserDTO(val id: Int, val name: String, val age: Int, val email: String)

val client = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }
}

fun main() {
    val scanner = Scanner(System.`in`)
    var exit = false

    while (!exit) {
        println(" -- Welcome to my app -- ")
        println("Select your option:")
        println("1. Get users")
        println("2. Get user by id")
        println("3. Add user")
        println("4. Update user by id")
        println("5. Delete user by id")
        println("6. Exit")
        val option: String = scanner.nextLine()

        when (option) {
            "1" -> {
                try {
                    getUsers()
                } catch (e: Exception) {
                    println("Error fetching users ${e.message}")
                }
            }
            "2" -> {
                println("Enter the id you want to search: ")
                val id = scanner.nextLine().trim().toIntOrNull()

                if (id != null) {
                    getUserById(id)
                } else {
                    println("Invalid id")
                }
            }

            "3" -> {
                println("Fill the following requirements")
                println("Use this format: id, name, age, email")
                val input = scanner.nextLine().trim().split(",").map { it.trim() }
                if (input.size == 4) {
                    val id = input[0].toIntOrNull()
                    val name = input[1]
                    val age = input[2].toIntOrNull()
                    val email = input[3]

                    if (id != null && age != null) {
                        val newUser = UserDTO(id, name, age, email)
                        addUser(newUser)
                    } else {
                        println("Invalid data")
                    }
                } else {
                    println("Incomplete data")
                }
            }

            "4" -> {
                println("Enter the id")
                val id = scanner.nextLine().trim().toIntOrNull() ?: return println("Invalid id")
                println("Add the new data to update")
                println("Use this format: id, name, age, email")
                val input = scanner.nextLine().trim().split(",").map { it.trim() }
                if (input.size == 4) {
                    val idNew = input[0].toIntOrNull()
                    val name = input[1]
                    val age = input[2].toIntOrNull()
                    val email = input[3]

                    if (idNew != null && age != null) {
                        val newUser = UserDTO(idNew, name, age, email)
                        updateUser(id, newUser)
                    } else {
                        println("Invalid data")
                    }
                } else {
                    println("Incomplete data")
                }
            }

            "5" -> {
                println("Enter the id you want to delete: ")
                val id = scanner.nextLine().trim().toIntOrNull()

                if (id != null) {
                    deleteUser(id)
                } else {
                    println("Invalid id")
                }
            }

            "6" -> exit = true

            else -> println("Invalid option")
        }
        //scanner.close()
    }
}

fun getUsers() = runBlocking {
    try {
        val response: HttpResponse = client.get("http://localhost:8080/user"){
            header(HttpHeaders.Accept, ContentType.Application.Json)
        }
        val users: String = response.bodyAsText()
        println(users)
        return@runBlocking users
    } catch (e: Exception) {
        println("Error fetching users, error: ${e.message}")
        return@runBlocking "Error fetching users, error: ${e.message}"
    }
}

fun getUserById(id: Int) = runBlocking {
    try {
        val response: HttpResponse = client.get("http://localhost:8080/user/$id"){
            header(HttpHeaders.Accept, ContentType.Application.Json)
        }
        val user: String = response.bodyAsText()
        println(user)
    } catch (e: Exception) {
        println("Error fetching user, error: ${e.message}")
    }
}

fun addUser(user: UserDTO) = runBlocking {
    try {
        val response: String = client.post("http://localhost:8080/user") {
            contentType(ContentType.Application.Json)
            setBody(user)
        }.body()
        println(response)
    } catch (e: Exception) {
        println("Error adding user, error: ${e.message}")
    }
}

fun updateUser(id: Int, user: UserDTO) = runBlocking {
    try {
        val response: String = client.patch("http://localhost:8080/user/$id") {
            contentType(ContentType.Application.Json)
            setBody(user)
        }.body()
        println(response)
    } catch (e: Exception) {
        println("Error updating user, error: ${e.message}")
    }
}

fun deleteUser(id: Int) = runBlocking {
    try {
        val response: String = client.delete("http://localhost:8080/user/$id").body()
        println(response)
    } catch (e: Exception) {
        println("Error deleting user, error: ${e.message}")
    }
}
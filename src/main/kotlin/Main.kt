import java.util.Scanner

fun main(args: Array<String>) {
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

        when (scanner.nextLine().trim()) {
            "1" -> getUsers()
            "2" -> {
                println("Enter the id you want to search: ")
                val id = scanner.nextLine().trim().toIntOrNull() != (null ?: return println("Invalid id"))
                getUserById(id)
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
                val id = scanner.nextLine().trim().toIntOrNull() != (null ?: return println("Invalid id"))
                deleteUser(id)
            }

            "6" -> exit = true

            else -> println("Invalid option")
        }
        scanner.close()
    }


    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString()}")
}
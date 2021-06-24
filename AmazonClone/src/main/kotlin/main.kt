import Models.Product
import Models.User

/*Amazon Clone is an app that will mimic the main functions that Amazon handles:
1.- Be able to register an user
2.- Be able to login an user
3.- Be able to upload products for sale
4.- Be able to buy products (complete checkout process*)

*The complete checkout process includes:
- Adding an item to the cart
- View the cart and the total
- Pay for the product
- Monitor the product delivery
* */

//TODO: Use user isLogged attribute instead of global session variable

//Variables to be used in our app:
//1.- For handling user registration
var registrationUsername: String = ""
var registrationEmail: String = ""
//var registeredEmails = arrayListOf<String>()
var registrationPassword: String = ""
var registrationPasswordConfirmation: String = ""
var registeredUsersList = arrayListOf<User>()

//2.- For handling logging and session
var username: String = ""
var password: String = ""
var session: Boolean = false
//var registeredUsers = mutableMapOf<String, String>()

//3.- For uploading a product
var productName: String = ""
var productCategory: String = ""
var validCategories = setOf("clothes", "technology", "home", "food", "health")
var productStatus: String = ""
var validStatus = setOf("new", "pre-owned", "owned")
var productDescription: String = ""
var productPrice: Float = 0f
var productAddedCorrectly: Boolean = false
//Consider substituting for an object. Simplifying implementation for now
var registeredProductsList = arrayListOf<Product>()

//4.- For buying a product
var productCart = listOf<String>()
var totalPrice = 0F
//Credit card details
var cardNumber: Long = 0
var cardName: String = ""
var cardDate: String = ""
var cardCVC: Byte = 0
//Ship to information
var firstName: String = ""
var lastName: String = ""
var addressLine: String = ""
var city: String = ""
var state: String = ""
var zipCode: Short = 0
var country: String = ""
var phoneNumber: Int = 0
var shipDate: String = ""
var trackingNumber: String = ""
var orderStatus: String = ""

//Function to validate if user username and password are correct
fun validCredentials(): Boolean {
    var valid: Boolean = false

    for (user in registeredUsersList)
        valid = user.username == username && user.password == password

    return valid
}

//Function to validate the required fields of the registration form
fun validRegistration(): Boolean {
    var valid: Boolean = false

    fun validateUsername(username: String): Boolean {
        return if (username == registrationUsername) {
            println("Username already exists, please enter a different one")
            false
        } else
            true
    }

    fun validateEmail(email: String): Boolean {
        return if (email == registrationEmail) {
            println("Email already exists, please enter a different one")
            false
        } else
            true
    }

    fun validatePasswords(): Boolean {
        return if (registrationPassword == registrationPasswordConfirmation)
            true
        else {
            println("Passwords don't match")
            false
        }
    }

    //If a username exists loop and check for duplicate
    //If not, just return true
    if (registeredUsersList.isEmpty())
        valid = validatePasswords()
    else {
        for (user in registeredUsersList)
            valid = validateUsername(user.username) && validateEmail(user.email) && validatePasswords()
    }

    return valid
}

//Function to check if a proper product was introduced
fun validateProduct(): Boolean {
    fun validCategory(): Boolean {
        return if (validCategories.contains(productCategory.lowercase()))
            true
        else {
            println("Please enter a valid category")
            false
        }
    }

    fun validStatus(): Boolean {
        return if (validStatus.contains(productStatus.lowercase()))
            true
        else {
            println("Please enter a valid status")
            false
        }
    }

    return validCategory() && validStatus()
}

//TODO: Validate for nulls
//TODO: Optimize code
//TODO: Validate input types (all read lines are strings now)
//TODO: Validate for user entering numbers bigger than byte maximum (-127 to 127)
//TODO: Add logout functionality
//TODO: Exit once logged in should get user back to main menu, not exit the app
fun main() {
    var firstOption: Byte = 1
    //Do while to keep the user iterating over the menu options till he decides to leave
    do {
        //Check to see if user has logged in
        if (!session){
            //Show welcome menu
            println("\nWelcome to <name in progress>!")
            println("What do you want to do?")
            println("1.- Login")
            println("2.- Register")
            println("3.- Exit")
            firstOption = readLine()?.toByte()!!

            //Decision path: login, register or exit
            when (firstOption) {
                1.toByte() -> {
                    //Login path
                    if (registeredUsersList.isEmpty()) {
                        println("No users have been registered in the system. \nPlease register one first before attempting to login")
                    } else {
                        do {
                            var secondOption: Byte
                            println("Please enter your username")
                            username = readLine()!!
                            println("Please enter your password")
                            password = readLine()!!
                            //Validate user input: if everything is ok, set session variable to true
                            session = validCredentials()
                            if (session){
                                println("Login successful! \nWelcome $username")
                                break
                            } else {
                                println("Username or password are incorrect!")
                                println("1.- Try again")
                                println("2.- Return to main menu")
                                secondOption = readLine()!!.toByte()
                            }
                        } while (secondOption != 2.toByte())
                    }
                }
                2.toByte() -> {
                    do {
                        //Register path
                        var thirdOption: Byte
                        println("Please enter an username")
                        registrationUsername = readLine()!!
                        println("Please enter an email")
                        //Extra validation, check user email structure
                        registrationEmail = readLine()!!
                        println("Please enter a password")
                        //Extra validation: Check user password structure
                        registrationPassword = readLine()!!
                        println("Please re-enter your password")
                        registrationPasswordConfirmation = readLine()!!

                        //If everything is ok, set session variable to true
                        session = validRegistration()
                        if (session){
                            //Add new user into the system
                            //Create new user Object and add it to the list of Users
                            //registeredUsers[registrationUsername] = registrationPassword
                            registeredUsersList.add(User(registrationUsername, registrationEmail, registrationPassword, true))
                            println("New user registered successfully! \nWelcome $registrationUsername")
                            break
                        } else {
                            println("1.- Try again")
                            println("2.- Return to main menu")
                            thirdOption = readLine()!!.toByte()
                        }
                    } while (thirdOption != 2.toByte())
                }
                3.toByte() -> firstOption = 3
                else -> println("Please enter a valid option")
            }
        } else {
            //Display articles list
            println("\nWhat would you like to do?")
            println("1.- Register an item")
            println("2.- Buy an item")
            println("3.- Exit")
            var fourthOption = readLine()!!.toByte()

            when(fourthOption) {
                1.toByte() -> {
                    do {
                        //Register path
                        var secondOption: Byte
                        //Extra validation: Check if product already exists
                        println("Please enter the product name")
                        productName = readLine()!!
                        println("Please enter the product category")
                        println("Available categories: clothes, technology, home, food or health")
                        //Validate a valid category is entered
                        productCategory = readLine()!!
                        println("Please enter the product status")
                        println("Product status options: new, pre-owned or used")
                        //Validate a valid category is entered
                        productStatus = readLine()!!
                        println("Please enter the product description")
                        //Extra validation: check for description length (i.e, 200 words)
                        productDescription = readLine()!!
                        println("Please enter the product price (in USD)")
                        productPrice = readLine()?.toFloat()!!

                        //If everything is ok, set productAddedCorrectly variable to true
                        productAddedCorrectly = validateProduct()
                        if (productAddedCorrectly){
                            //Add new product into the system
                            registeredProductsList.add(Product(productName, productCategory, productStatus, productDescription, productPrice))
                            println("New product registered successfully!")
                            break
                        } else {
                            println("1.- Try again")
                            println("2.- Return to main menu")
                            secondOption = readLine()!!.toByte()
                        }
                    } while (secondOption != 2.toByte())
                }
                2.toByte() -> {
                    //TODO: Define logic for buying a product
                }
                3.toByte() -> session = false
                else -> println("Please enter a valid option")
            }
        }

    } while (firstOption != 3.toByte())

    //Goodbye message
    println("Thanks for using our app")
}



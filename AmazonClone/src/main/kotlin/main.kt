import models.Card
import models.Cart
import models.Product
import models.User
import java.lang.NumberFormatException
import java.util.regex.Pattern
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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

//If code fails to compile because Redeclaration error, use: ./gradlew clean

//TODO: Optimize isLogged property usage
//TODO: Implement functions for general use business logic (repetitive ones)
//TODO: Optimize code
//TODO: Check postworks and challenges to see what else to add our code
//TODO: Protect strings if user enters a number instead
//TODO: Make islogged the name of the Boolean variable in user to follow the standard convention
//TODO: Check to see what happens if user types another invalid option in any input read
//TODO: Make input text lowecase if needed
//TODO: Add test products for buying (2-3 per category)
//TODO: Allow the user to track their current orders being shipped
//TODO: Implement cart for multiuser application (like user login)

//Variables to be used in our app:
//1.- For handling user registration
var registrationUsername: String = ""
var registrationEmail: String = ""
//var registeredEmails = arrayListOf<String>()
var registrationPassword: String = ""
var registrationPasswordConfirmation: String = ""
var registeredUsersList = arrayListOf<User>()

//2.- For handling logging and session
//Use ? to let Kotlin know that this value can be null (i.e. User typing enter without entering a value)
var username: String? = ""
var password: String? = ""
var currentUser: Byte = 0
//var session: Boolean = false
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
var cart: Cart? = null
var productCart = mutableListOf<Product>()
//Credit card details
var cardNumber: Long = 0
var cardName: String = ""
var cardDate: String = ""
var cardCVC: Short = 0
//Ship to information
var firstName: String = ""
var lastName: String = ""
var addressLine: String = ""
var city: String = ""
var state: String = ""
var zipCode: Int = 0
var country: String = ""
var phoneNumber: Long = 0

//Function to validate if user username and password are correct
fun validCredentials() {

    if (username == "" || password == "")
        println("Neither the username nor password can be empty\nPlease try again\n")
    else {
        for ((index, oneUser) in registeredUsersList.withIndex()) {
            if (oneUser.getUsername() == username && oneUser.getPassword() == password){
                oneUser.setIsLogged(true)
                currentUser = index.toByte()
            }
        }
    }

}

//Function to validate the required fields of the registration form
fun validRegistration(): Boolean {
    var valid = false

    fun notNullInput(): Boolean {
        return if (registrationUsername.isBlank() || registrationEmail.isBlank() || registrationPassword.isBlank() || registrationPasswordConfirmation.isBlank()) {
            println("Input data cannot be null. Please renter the information\n")
            false
        } else
            true
    }

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

    //Function to validate email address structure
    fun isEmailValid(email: String): Boolean {
        return if (Pattern.compile(
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ).matcher(email).matches())
            true
        else {
            println("Email structure invalid, please follow email@domain.com")
            false
        }
    }

    fun isPasswordSafe(password: String): Boolean{
        return if (Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,}" +               //at least 8 characters
                    "$").matcher(password).matches())
            true
        else {
            println("Password structure invalid, please follow this guidelines:")
            println("At least one digit (0-9)")
            println("At least one lower case letter (a-z)")
            println("At least one upper case letter (A-Z)")
            println("At least one special character (@#\\$%^&+=)")
            println("No white spaces")
            println("At least 8 characters")
            false
        }
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
        valid = isEmailValid(registrationEmail) && validatePasswords() && isPasswordSafe(registrationPassword)
    else {
        for (oneUser in registeredUsersList) {
            if(validateUsername(oneUser.getUsername()) && validateEmail(oneUser.email) && isEmailValid(registrationEmail) && validatePasswords() && isPasswordSafe(registrationPassword)){
                valid = true
            }
        }
    }

    return notNullInput() && valid
}

//Function to check if a proper product was introduced
fun validateProduct(): Boolean {

    fun notNullInput(): Boolean {
        return if (productName.isBlank() || productCategory.isBlank() || productStatus.isBlank() || productDescription.isBlank()) {
            println("Input data cannot be null. Please renter the information\n")
            false
        } else
            true
    }

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

    fun validDescription(): Boolean {
        return if (productDescription.length > 200){
            println("Product description should be less than 200 words")
            false
        }
        else
            true
    }

    return notNullInput() && validCategory() && validStatus() && validDescription()
}

fun userLogged(): Boolean {
    var valid = false

    //If a username exists loop and check for duplicate
    //If not, just return true
    if (registeredUsersList.isNotEmpty()){
        for (oneUser in registeredUsersList){
            if(oneUser.isLogged())
                valid = true
        }
    }

    return valid
}

fun logout() {
    registeredUsersList.elementAt(currentUser.toInt()).setIsLogged(false)
    currentUser = 0
}

fun displayCurrentCart(): Boolean {
    return if(productCart.isEmpty()){
        println("No items have been added yet to the cart\n")
        false
    }
    else {
        println("Current items in cart: ")
        println("    Product name - Category - Status - Description - Price")
        productCart.forEachIndexed { index, element -> println("${index + 1}.- ${element.productInformation()}") }
        println()
        true
    }
}

fun displayRegisteredItems(): Boolean {
    return if(registeredProductsList.isEmpty()){
        println("No items have been added, sorry for the inconveniences")
        println("Please add an item first\n")
        false
    } else {
        displayCurrentCart()
        println("Please select the item number you would like to buy: ")
        println("    Product name - Category - Status - Description - Price")
        registeredProductsList.forEachIndexed { index, element -> println("${index + 1}.- ${element.productInformation()}") }
        true
    }
}

fun deleteItemsFromCart(deleteOption: Byte) {
    if(deleteOption == 0.toByte())
        println("Nothing was deleted")
    else {
        try {
            productCart.removeAt(deleteOption.toInt() - 1)
            println("Element $deleteOption deleted successfully")
            displayCurrentCart()
        } catch (e: IndexOutOfBoundsException) {
            println("Specified element doesn't exist. Unable to remove it\n")
        }
    }
}

//Function to check if a proper product was introduced
fun validateUserInformation(): Boolean {

    fun notNullInput(): Boolean {
        return if (firstName.isBlank() || lastName.isBlank() || addressLine.isBlank() || city.isBlank() || state.isBlank() || country.isBlank()) {
            println("Input data cannot be null. Please renter the information\n")
            false
        } else
            true
    }

    return notNullInput()
}

//To check the credit card length
fun checkCardLength() {
    val cardLength: String = cardNumber.toString()
    if (cardLength.length != 16)
        throw NumberFormatException()
}

//To check the credit card CVC length
fun checkCardCVCLength() {
    val cardCVCLength: String = cardCVC.toString()
    if (cardCVCLength.length != 3)
        throw NumberFormatException()
}

//Function to check if proper card information was introduced
fun validateCardInformation(): Boolean {

    fun notNullInput(): Boolean {
        return if (cardName.isBlank()) {
            println("Card name cannot be null. Please renter the information\n")
            false
        } else
            true
    }

    return notNullInput()
}

//Function to hardcode the shipping information
fun generateShippingInformation() {
    //ShipDate
    val currentDateTime = LocalDate.now()
    currentDateTime.plusDays(7)
    cart?.shipDate = currentDateTime.format(DateTimeFormatter.ofPattern("dd-MM-yy"))
    println("\nYou will receive your order at ${cart!!.shipDate}")

    //Tracking number
    cart?.trackingNumber = (0..100_000_000_000).random() 

    //Order status
    cart?.orderStatus = "Shipped"
}

fun main() {
    //For testing purposes
    registeredUsersList.add(User("juanma", "juan@test.com", "Ju4nM4#45"))
    registeredProductsList.add(Product("test product", "home", "new", "testing description length", 12f))
    var firstOption: Byte = 1
    //Do while to keep the user iterating over the menu options till he decides to leave
    do {
        //Check to see if user has logged in
        if (!userLogged()){
            //Show welcome menu
            println("\nWelcome to <name in progress>!")
            println("Get your products within a week")
            println("What do you want to do?")
            println("1.- Login")
            println("2.- Register")
            println("3.- Exit")
            firstOption = try {
                readLine()!!.toByte()
            } catch (e: NumberFormatException){
                4
            }

            //Decision path: login, register or exit
            when (firstOption) {
                1.toByte() -> {
                    //Login path
                    if (registeredUsersList.isEmpty()) {
                        println("No users have been registered in the system. \nPlease register one first before attempting to login")
                    } else {
                        do {
                            var secondOption: Byte = 3
                            println("Please enter your username")
                            //Check if the value read is null or not. If it is, then assign ""
                            username = readLine()?:""
                            println("Please enter your password")
                            //Check if the value read is null or not. If it is, then assign ""
                            password = readLine()?:""
                            //Validate user input: if everything is ok, set session variable to true
                            validCredentials()
                            //Surround with try-catch to prevent the user from accessing an inexistent element in the registered user's array
                            try {
                                if (registeredUsersList.elementAt(currentUser.toInt()).isLogged()){
                                    println("Login successful! \nWelcome $username")
                                    break
                                } else {
                                    if (!(username == "" || password == "")) {
                                        println("Username or password are incorrect!")
                                        println("1.- Try again")
                                        println("2.- Return to main menu")
                                        secondOption = try {
                                            readLine()!!.toByte()
                                        } catch (e: NumberFormatException){
                                            println("Option not valid, returning to main menu")
                                            2
                                        }
                                    }
                                }
                            } catch (e: ArrayIndexOutOfBoundsException) {
                                println("Not such element exists in the system. Please try again")
                                secondOption = 1
                            }
                        } while (secondOption != 2.toByte())
                    }
                }
                2.toByte() -> {
                    do {
                        //Register path
                        var thirdOption: Byte = 3
                        println("Please enter an username")
                        //Check if the value read is null or not. If it is, then assign ""
                        registrationUsername = readLine()?:""
                        println("Please enter an email")
                        //Check if the value read is null or not. If it is, then assign ""
                        registrationEmail = readLine()?:""
                        println("Follow this guidelines to create a password:")
                        println("At least one digit (0-9)")
                        println("At least one lower case letter (a-z)")
                        println("At least one upper case letter (A-Z)")
                        println("At least one special character (@#\\$%^&+=)")
                        println("No white spaces")
                        println("At least 8 characters")
                        println("Please enter a password")
                        //Check if the value read is null or not. If it is, then assign ""
                        registrationPassword = readLine()?:""
                        println("Please re-enter your password")
                        //Check if the value read is null or not. If it is, then assign ""
                        registrationPasswordConfirmation = readLine()?:""

                        //If everything is ok, set session variable to true
                        if (validRegistration()){
                            //Add new user into the system
                            //Create new user Object and add it to the list of Users
                            //registeredUsers[registrationUsername] = registrationPassword
                            registeredUsersList.add(User(registrationUsername, registrationEmail, registrationPassword, true))
                            println("New user registered successfully! \nWelcome $registrationUsername")
                            break
                        } else {
                            if(!(registrationUsername == "" || registrationEmail == "" || registrationPassword == "" || registrationPasswordConfirmation == "")){
                                println("1.- Try again")
                                println("2.- Return to main menu")
                                thirdOption = try {
                                    readLine()!!.toByte()
                                } catch (e: NumberFormatException){
                                    println("Option not valid, returning to main menu")
                                    2
                                }
                            }
                        }
                    } while (thirdOption != 2.toByte())
                }
                3.toByte() -> firstOption = 3
                else -> println("Please enter a valid and not-null option")
            }
        } else {
            //Display articles list
            println("\nWhat would you like to do?")
            println("1.- Register an item")
            println("2.- Add/Remove items from the cart")
            println("3.- Checkout process")
            println("4.- Logout")
            val fourthOption: Byte = try {
                readLine()!!.toByte()
            } catch (e: NumberFormatException){
                5
            }

            when(fourthOption) {
                1.toByte() -> {
                    do {
                        //Register path
                        var secondOption: Byte = 3
                        println("Please enter the product name")
                        //Check if the value read is null or not. If it is, then assign ""
                        productName = readLine()?:""
                        println("Please enter the product category")
                        println("Available categories: clothes, technology, home, food or health")
                        //Check if the value read is null or not. If it is, then assign ""
                        productCategory = readLine()?:""
                        println("Please enter the product status")
                        println("Product status options: new, pre-owned or owned")
                        //Check if the value read is null or not. If it is, then assign ""
                        productStatus = readLine()?:""
                        println("Please enter the product description")
                        //Check if the value read is null or not. If it is, then assign ""
                        productDescription = readLine()?:""
                        println("Please enter the product price (in USD)")
                        var test: Boolean
                        do {
                            try {
                                productPrice = readLine()!!.toFloat()
                                test = true
                            } catch (e: NumberFormatException){
                                println("Value invalid, please enter a valid price")
                                test = false
                            }
                        } while (!test)

                        //If everything is ok, set productAddedCorrectly variable to true
                        productAddedCorrectly = validateProduct()
                        if (productAddedCorrectly){
                            //Add new product into the system
                            registeredProductsList.add(Product(productName, productCategory, productStatus, productDescription, productPrice))
                            println("New product registered successfully!")
                            break
                        } else {
                            if(!(productName == "" || productCategory == "" || productStatus == "" || productDescription == "")) {
                                println("1.- Try again")
                                println("2.- Return to main menu")
                                secondOption = try {
                                    readLine()!!.toByte()
                                } catch (e: NumberFormatException) {
                                    println("Option not valid, returning to main menu")
                                    2
                                }
                            }
                        }
                    } while (secondOption != 2.toByte())
                }
                2.toByte() -> {
                    //Adding items to the cart path
                    //To know which item the user wants to buy
                    var selectedItem: Byte = 0
                    //To see what the user wants to do
                    var selectedOption: Byte
                    //To loop while the user wants to continue adding items to his/her cart
                    do {
                        //Display menu options
                        println("Please select what you want to do: ")
                        println("1.- Buy an item")
                        println("2.- Delete an item from the cart")
                        println("3.- Return to previous menu")
                        selectedOption = try {
                            readLine()!!.toByte()
                        } catch (e: NumberFormatException){
                            println("Option not valid, please select a valid option\n")
                            4
                        }
                        //Do something depending on user selection
                        when(selectedOption) {
                            1.toByte() -> {
                                if(displayRegisteredItems()){
                                    try {
                                        selectedItem = readLine()!!.toByte()
                                    } catch (e: NumberFormatException){
                                        println("Option not valid, returning to previous menu")
                                        break
                                    }
                                    //Check to see if the selected number matches an existing product
                                    if(selectedItem <= registeredProductsList.size) { //Exists
                                        productCart.add(registeredProductsList[selectedItem.toInt() - 1])
                                        println("Product added successfully to the cart\n")
                                    } else
                                        println("Unable to add the product\nPlease select a valid option")
                                }
                            }
                            2.toByte() -> {
                                if(displayCurrentCart()) {
                                    var deleteOption: Byte
                                    println("Please introduce the element number you wish to delete")
                                    try {
                                        deleteOption = readLine()!!.toByte()
                                        deleteItemsFromCart(deleteOption)
                                    } catch (e: NumberFormatException) {
                                        println("Option not valid, returning to previous menu\n")
                                    }
                                }
                            }
                            3.toByte() -> { break }
                            else -> println("Option not valid, please select a valid option\n")
                        }
                    } while (selectedItem != 3.toByte())
                }
                3.toByte() -> {
                    //Checkout path
                    if(productCart.isEmpty()){
                        println("No products have been added to the cart yet. Please add a product first before proceeding to checkout.")
                    } else {
                        var secondOption: Byte
                        var election: Byte
                        if (cart == null){
                            //Display the total and ask the user if they want to proceed
                            cart = Cart(productCart)
                        }
                        cart!!.calculateTotalPrice()
                        println("The total of the order is ${cart!!.totalPrice}")
                        do {
                            println("Do you want to proceed?")
                            println("1.- Yes")
                            println("2.- Go back to previous menu")
                            election = try {
                                readLine()!!.toByte()
                            } catch (e: NumberFormatException) {
                                println("Option not valid, returning to previous menu")
                                2
                            }
                            if (!(election == 1.toByte() || election == 2.toByte())) {
                                println("Option not valid, please try again")
                                election = 3
                            }
                        } while (!(election == 1.toByte() || election == 2.toByte()))
                        //Prompt the user for additional information to be able to ship the order
                        if(election == 1.toByte()){
                            //TODO: If user information already exists give the user the option to edit it (personal or payment)
                            //Check if the user information is already added
                            if (registeredUsersList[currentUser.toInt()].firstName == "") {
                                do {
                                    println("Please introduce your first name")
                                    firstName = readLine()?:""
                                    println("Please introduce your last name")
                                    lastName = readLine()?:""
                                    println("Please introduce your address")
                                    addressLine = readLine()?:""
                                    println("Please introduce your city")
                                    city = readLine()?:""
                                    println("Please introduce your state")
                                    state = readLine()?:""
                                    println("Please introduce your zip code")
                                    var test: Boolean
                                    do {
                                        try {
                                            zipCode = readLine()!!.toInt()
                                            test = true
                                        } catch (e: NumberFormatException){
                                            println("Value invalid, please enter a valid zipCode")
                                            test = false
                                        }
                                    } while (!test)
                                    println("Please introduce your country")
                                    country = readLine()?:""
                                    println("Please introduce your phone number")
                                    var test2: Boolean
                                    do {
                                        try {
                                            phoneNumber = readLine()!!.toLong()
                                            test2 = true
                                        } catch (e: NumberFormatException){
                                            println("Value invalid, please enter a valid zipCode")
                                            test2 = false
                                        }
                                    } while (!test2)

                                    //If everything is ok, move on
                                    if (validateUserInformation()){
                                        //Add user information
                                        registeredUsersList[currentUser.toInt()].firstName = firstName
                                        registeredUsersList[currentUser.toInt()].lastName = lastName
                                        registeredUsersList[currentUser.toInt()].addressLine = addressLine
                                        registeredUsersList[currentUser.toInt()].city = city
                                        registeredUsersList[currentUser.toInt()].state = state
                                        registeredUsersList[currentUser.toInt()].zipCode = zipCode
                                        registeredUsersList[currentUser.toInt()].country = country
                                        registeredUsersList[currentUser.toInt()].phoneNumber = phoneNumber
                                        println("User profile completed successfully!\n")
                                        break
                                    } else {
                                        println("1.- Try again")
                                        println("2.- Return to main menu")
                                        secondOption = try {
                                            readLine()!!.toByte()
                                        } catch (e: NumberFormatException) {
                                            println("Option not valid, returning to main menu")
                                            2
                                        }
                                    }
                                } while (secondOption != 2.toByte())
                            } else
                                println("User profile is completed. Proceeding with payment information\n")

                            //Now ask the user for his credit card information if no previous information exists
                            if(registeredUsersList[currentUser.toInt()].card == null) {
                                do {
                                    println("Please introduce your credit card information")
                                    println("Please introduce the cardholder name")
                                    cardName = readLine()?:""
                                    println("Please introduce the card 16 digits")
                                    var testCard: Boolean
                                    do {
                                        try {
                                            cardNumber = readLine()!!.toLong()
                                            checkCardLength()
                                            testCard = true
                                        } catch (e: NumberFormatException){
                                            println("Value invalid, please enter a valid card number (16 digits long)")
                                            testCard = false
                                        }
                                    } while (!testCard)
                                    //TODO: Validate date format
                                    println("Please introduce the card expiration date (mm/yy)")
                                    cardDate = readLine()?:""
                                    println("Please introduce your card CVC")
                                    var testCardCVC: Boolean
                                    do {
                                        try {
                                            cardCVC = readLine()!!.toShort()
                                            checkCardCVCLength()
                                            testCardCVC = true
                                        } catch (e: NumberFormatException){
                                            println("Value invalid, please enter a valid card CVC (3 digits long)")
                                            testCardCVC = false
                                        }
                                    } while (!testCardCVC)
                                    //TODO: Ask the user to enter his billing information

                                    //If everything is ok, move on
                                    if (validateCardInformation()){
                                        //Add card information into the current user
                                        registeredUsersList[currentUser.toInt()].card = Card(cardNumber, cardName, cardDate, cardCVC)
                                        println("Card added successfully to user profile!")
                                        break
                                    } else {
                                        println("1.- Try again")
                                        println("2.- Return to main menu")
                                        secondOption = try {
                                            readLine()!!.toByte()
                                        } catch (e: NumberFormatException) {
                                            println("Option not valid, returning to main menu")
                                            2
                                        }
                                    }
                                } while (secondOption != 2.toByte())
                            } else
                                println("Card information for current user exists in the system. Proceeding with payment\n")

                            //Payment section
                            //Async section
                            //Assuming payment will be successful. TODO: Random number to make path choices (card accepted or denied)
                            println("Payment successful! ")
                            generateShippingInformation()
                        }
                    }
                }
                4.toByte() -> {
                    //Logout path
                    logout()
                }
                else -> println("Please enter a valid and not-null option")
            }
        }

    } while (firstOption != 3.toByte())

    //Goodbye message
    println("Thanks for using our app")
}




package models

data class User (private val username: String, var email: String, private var password: String, private var logged: Boolean = false,
                 var firstName: String = "", var lastName: String = "", var addressLine: String = "",
                 var city: String = "", var state: String = "", var zipCode: Short = 0,
                 var country: String = "", var phoneNumber: Int = 0, var card: Card = Card()) {
    fun getUsername(): String {
        return username
    }

    fun getPassword(): String {
        return password
    }

    fun setIsLogged(logged: Boolean){
        this.logged = logged
    }

    fun isLogged(): Boolean {
        return logged
    }
}
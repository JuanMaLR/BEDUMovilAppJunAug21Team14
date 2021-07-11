package models

data class User (private val username: String, var email: String, private var password: String, private var isLogged: Boolean = false,
                 var firstName: String = "", var lastName: String = "", var addressLine: String = "",
                 var city: String = "", var state: String = "", var zipCode: Int = 0,
                 var country: String = "", var phoneNumber: Long = 0, var card: Card? = null) {
    fun getUsername(): String {
        return username
    }

    fun getPassword(): String {
        return password
    }

    fun setIsLogged(logged: Boolean){
        this.isLogged = logged
    }

    fun isLogged(): Boolean {
        return isLogged
    }
}
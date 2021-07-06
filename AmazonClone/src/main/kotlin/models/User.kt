package models

data class User (val username: String, var email: String, var password: String, var isLogged: Boolean = false,
            var firstName: String = "", var lastName: String = "", var addressLine: String = "",
            var city: String = "", var state: String = "", var zipCode: String = "",
            var country: String = "", var phoneNumber: String = "")
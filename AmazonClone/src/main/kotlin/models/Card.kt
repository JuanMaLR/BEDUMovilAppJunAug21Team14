package models

data class Card (var number: Long = 0L, var cardholderName: String = "", var expirationDate: String = "", var cvc: Byte = 0)
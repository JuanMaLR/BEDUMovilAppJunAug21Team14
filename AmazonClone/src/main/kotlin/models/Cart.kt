package models

data class Cart (val productList: MutableList<Product> = mutableListOf(), var totalPrice: Float, var shipDate: String = "", var trackingNumber: String = "", var orderStatus: String = "")
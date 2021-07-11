package models

class Cart (private val productList: MutableList<Product> = mutableListOf(), var totalPrice: Float = 0f, var shipDate: String = "", var trackingNumber: Long = 0, var orderStatus: String = "") {
    fun calculateTotalPrice() {
        productList.forEach { totalPrice += it.price }
    }
}
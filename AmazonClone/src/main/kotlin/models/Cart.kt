package models

class Cart (val productList: MutableList<Product> = mutableListOf(), var totalPrice: Float = 0f, var shipDate: String = "", var trackingNumber: String = "", var orderStatus: String = "") {
    fun calculateTotalPrice() {
        productList.forEach { totalPrice += it.price }
    }
}
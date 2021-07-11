package models

class Product (private val name: String, var category: String, var status: String, var description: String, var price: Float) {
    fun productInformation(): String {
        return "$name - $category - $status - ${description.take(50)}... - $$price"
    }

    fun getName(): String {
        return name
    }
}
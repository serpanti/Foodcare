package ru.foodcare.foodcare.domain

interface ProductRepository {
    fun addProduct(product: Product)
    fun removeProduct(product: Product)
    fun updateProduct(newProduct: Product, oldProduct: Product)
    fun getProducts(): List<Product>
    fun getProduct(name: String, production: String): List<Product>
    fun getProductByName(name: String): List<Product>
    fun getProductByProduction(production: String): List<Product>
}
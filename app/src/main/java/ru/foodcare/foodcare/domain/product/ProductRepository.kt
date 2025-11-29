package ru.foodcare.foodcare.domain.product

import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun addProduct(product: Product)
    suspend fun removeProduct(product: Product)
    suspend fun updateProduct(newProduct: Product, oldProduct: Product)
    suspend fun getProducts(): List<Product>
    fun observeProducts(): Flow<List<Product>>
    suspend fun getProduct(name: String, production: String): List<Product>
    fun observeProductsByName(name: String): Flow<List<Product>>
    fun observeProductsByProduction(production: String): Flow<List<Product>>
    suspend fun getProductByName(name: String): List<Product>
    suspend fun getProductByProduction(production: String): List<Product>
}
package ru.foodcare.foodcare.data.product

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDAO {
    @Insert(onConflict = IGNORE)
    suspend fun addProduct(product: Product)

    @Delete
    suspend fun removeProduct(product: Product)

    @Update(onConflict = IGNORE)
    suspend fun updateProduct(product: Product)

    @Query("SELECT * FROM products")
    suspend fun getProducts(): List<Product>

    @Query("SELECT * FROM products")
    fun observeProducts(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE name = :name AND production = :production")
    suspend fun getProduct(name: String, production: String): List<Product>

    @Query("SELECT * FROM products WHERE name LIKE :name || '%' COLLATE NOCASE")
    suspend fun getProductByName(name: String): List<Product>

    @Query("SELECT * FROM products WHERE production LIKE :production || '%' COLLATE NOCASE")
    suspend fun getProductByProduction(production: String): List<Product>
}
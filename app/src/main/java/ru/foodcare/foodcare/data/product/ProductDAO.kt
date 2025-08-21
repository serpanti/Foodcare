package ru.foodcare.foodcare.data.product

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProductDAO {
    @Insert(onConflict = IGNORE)
    fun addProduct(product: Product)

    @Delete
    fun removeProduct(product: Product)

    @Update
    fun updateProduct(product: Product)

    @Query("SELECT * FROM products")
    fun getProducts(): List<Product>

    @Query("SELECT * FROM products WHERE name = :name AND production = :production")
    fun getProduct(name: String, production: String): List<Product>

    @Query("SELECT * FROM products WHERE name LIKE :name || '%' COLLATE NOCASE")
    fun getProductByName(name: String): List<Product>

    @Query("SELECT * FROM products WHERE production LIKE :production || '%' COLLATE NOCASE")
    fun getProductByProduction(production: String): List<Product>
}
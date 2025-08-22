package ru.foodcare.foodcare.data.product

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.Query

@Dao
interface ProductDAO {
    @Insert(onConflict = IGNORE)
    suspend fun addProduct(product: Product)

    @Delete
    suspend fun removeProduct(product: Product)

    @Query("""
    UPDATE products SET 
        name = :name,
        production = :production,
        amount = :amount,
        type = :type,
        calories = :calories,
        protein = :protein,
        fat = :fat,
        carbohydrates = :carbohydrates,
        fiber = :fiber
    WHERE name = :oldName AND production = :oldProduction
""")
    suspend fun updateProduct(name: String,
                      production: String,
                      amount: Int,
                      type: String,
                      calories: Double,
                      protein: Double,
                      fat: Double,
                      carbohydrates: Double,
                      fiber: Double,
                      oldName: String,
                      oldProduction: String
    )

    @Query("SELECT * FROM products")
    suspend fun getProducts(): List<Product>

    @Query("SELECT * FROM products WHERE name = :name AND production = :production")
    suspend fun getProduct(name: String, production: String): List<Product>

    @Query("SELECT * FROM products WHERE name LIKE :name || '%' COLLATE NOCASE")
    suspend fun getProductByName(name: String): List<Product>

    @Query("SELECT * FROM products WHERE production LIKE :production || '%' COLLATE NOCASE")
    suspend fun getProductByProduction(production: String): List<Product>
}
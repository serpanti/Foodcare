package ru.foodcare.foodcare.data.meal

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ru.foodcare.foodcare.data.product.Product

@Dao
interface MealDAO {
    @Query("SELECT DISTINCT year FROM meals ORDER BY year ASC")
    fun observeYears(): Flow<List<Int>>

    @Query("""SELECT DISTINCT month FROM meals
        WHERE year = :year
        ORDER BY month ASC""")
    fun observeMonths(year: Int): Flow<List<Int>>

    @Query("""SELECT DISTINCT day FROM meals
        WHERE year = :year AND month = :month
        ORDER BY day ASC""")
    fun observeDays(year: Int, month: Int): Flow<List<Int>>

    @Transaction
    @Query("""SELECT * FROM meals
        WHERE year = :year AND month = :month AND day = :day
        ORDER BY hours, minutes, seconds ASC""")
    fun observeDay(year: Int, month: Int, day: Int): Flow<List<MealWithProduct>>

    @Insert(onConflict = IGNORE)
    suspend fun addMeal(meal: Meal)

    @Delete
    suspend fun removeMeal(meal: Meal)

    @Update(onConflict = IGNORE)
    suspend fun updateMeal(meal: Meal)
}

data class MealWithProduct(
    @Embedded val meal: Meal,
    @Relation (Product::class, parentColumn = "productId", entityColumn = "id")
    val product: Product
)

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
import ru.foodcare.foodcare.data.date.Date
import ru.foodcare.foodcare.data.product.Product

@Dao
interface MealDAO {
    @Transaction
    @Query("""SELECT * FROM meals
        WHERE dateId = :dateId
        ORDER BY hours, minutes, seconds ASC""")
    fun observeDay(dateId: Int): Flow<List<MealWithProductAndDate>>

    @Insert(onConflict = IGNORE)
    suspend fun addMeal(meal: Meal)

    @Delete
    suspend fun removeMeal(meal: Meal)

    @Update(onConflict = IGNORE)
    suspend fun updateMeal(meal: Meal)
}

data class MealWithProductAndDate(
    @Embedded val meal: Meal,
    @Relation (Product::class, parentColumn = "productId", entityColumn = "id")
    val product: Product,
    @Relation (Date::class, parentColumn = "dateId", entityColumn = "id")
    val date: Date
)

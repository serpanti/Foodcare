package ru.foodcare.foodcare.data.weightMeasurement

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

@Dao
interface WeightMeasurementDAO {
    @Transaction
    @Query("""SELECT * FROM dates WHERE id = :dateId""")
    fun observeDate(dateId: Int): Flow<List<DateWithWeightMeasurements>>

    @Insert(onConflict = IGNORE)
    suspend fun addWeightMeasurement(weightMeasurement: WeightMeasurement)

    @Delete
    suspend fun removeWeightMeasurement(weightMeasurement: WeightMeasurement)

    @Update(onConflict = IGNORE)
    suspend fun updateWeightMeasurement(weightMeasurement: WeightMeasurement)
}

data class DateWithWeightMeasurements(
    @Embedded val date: Date,
    @Relation (
        parentColumn = "id",
        entityColumn = "dateId"
    )
    val weightMeasurements: List<WeightMeasurement>
)

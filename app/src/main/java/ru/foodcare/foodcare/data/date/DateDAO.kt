package ru.foodcare.foodcare.data.date

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DateDAO {
    @Query("SELECT * FROM dates ORDER BY year ASC, month ASC, day ASC")
    fun observeDates(): Flow<List<Date>>

    @Query("SELECT DISTINCT year FROM dates ORDER BY year ASC")
    fun observeYears(): Flow<List<Int>>

    @Query("""SELECT DISTINCT month FROM dates
        WHERE year = :year
        ORDER BY month ASC""")
    fun observeMonths(year: Int): Flow<List<Int>>

    @Query("""SELECT day FROM dates
        WHERE year = :year AND month = :month
        ORDER BY day ASC""")
    fun observeDays(year: Int, month: Int): Flow<List<Int>>

    @Query("""SELECT * FROM dates
        WHERE year = :year AND month = :month AND day = :day""")
    fun getDate(year: Int, month: Int, day: Int): Flow<List<Date>>

    @Query("""SELECT * FROM dates
        WHERE year = :year AND month = :month AND day = :day""")
    suspend fun getSuspendDate(year: Int, month: Int, day: Int): List<Date>

    @Insert(onConflict = IGNORE)
    suspend fun addDate(date: Date)

    @Delete
    suspend fun removeDate(date: Date)

    @Update(onConflict = IGNORE)
    suspend fun updateDate(date: Date)

    @Transaction
    suspend fun getDateAndInsert(year: Int, month: Int, day: Int): List<Date> {
        val date = getSuspendDate(year, month, day)
        return if (date.isEmpty())  {
            addDate(Date(0, year, month, day))
            getSuspendDate(year, month, day)
        } else {
            date
        }
    }
}

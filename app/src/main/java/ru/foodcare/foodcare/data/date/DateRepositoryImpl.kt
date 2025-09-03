package ru.foodcare.foodcare.data.date

import kotlinx.coroutines.flow.Flow
import ru.foodcare.foodcare.domain.date.DateRepository

class DateRepositoryImpl(private val dateDao: DateDAO) : DateRepository {
    override fun observeYears(): Flow<List<Int>> {
        return dateDao.observeYears()
    }

    override fun observeMonths(year: Int): Flow<List<Int>> {
        return dateDao.observeMonths(year)
    }

    override fun observeDays(
        year: Int,
        month: Int
    ): Flow<List<Int>> {
        return dateDao.observeDays(year, month)
    }
}
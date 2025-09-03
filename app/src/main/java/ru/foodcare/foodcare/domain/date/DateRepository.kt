package ru.foodcare.foodcare.domain.date

import kotlinx.coroutines.flow.Flow

interface DateRepository {
    fun observeYears(): Flow<List<Int>>
    fun observeMonths(year: Int): Flow<List<Int>>
    fun observeDays(year: Int, month: Int): Flow<List<Int>>
}
package ru.foodcare.foodcare.domain.weight

import kotlinx.coroutines.flow.Flow

interface WeightRepository {
    fun observeDay(year: Int, month: Int, day: Int): Flow<List<Weight>>
    suspend fun add(weight: Weight)
    suspend fun remove(weight: Weight)
    suspend fun update(weight: Weight)
}
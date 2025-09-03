package ru.foodcare.foodcare.data.weightMeasurement

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import ru.foodcare.foodcare.data.date.DateDAO
import ru.foodcare.foodcare.domain.weight.Weight
import ru.foodcare.foodcare.domain.weight.WeightRepository
import kotlin.collections.firstOrNull

class WeightRepositoryImpl(private val weightDao: WeightMeasurementDAO,
    private val dateDAO: DateDAO): WeightRepository {
    override fun observeDay(
        year: Int,
        month: Int,
        day: Int
    ): Flow<List<Weight>> {
        return dateDAO.getDate(year, month, day)
            .map { it.firstOrNull()?.id }
            .filterNotNull()
            .flatMapLatest { id ->
                weightDao.observeDate(id)
            }
            .map { list ->
                list.firstOrNull() ?. let {
                    WeightMeasurementMapper.toDomain(it)
                }
            }
            .filterNotNull()
    }

    override suspend fun add(weight: Weight) {
        val dataId = dateDAO.getDateAndInsert(weight.date.year,
            weight.date.monthValue,
            weight.date.dayOfMonth).firstOrNull()?.id
        dataId?.let { dateId ->
            weightDao.addWeightMeasurement(WeightMeasurementMapper.fromDomain(weight, dateId))
        }
    }

    override suspend fun remove(weight: Weight) {
        val dataId = dateDAO.getSuspendDate(weight.date.year,
            weight.date.monthValue,
            weight.date.dayOfMonth).firstOrNull()?.id
        dataId?.let { dateId ->
            weightDao.removeWeightMeasurement(WeightMeasurementMapper.fromDomain(weight, dateId))
        }
    }

    override suspend fun update(weight: Weight) {
        val dataId = dateDAO.getSuspendDate(weight.date.year,
            weight.date.monthValue,
            weight.date.dayOfMonth).firstOrNull()?.id
        dataId?.let { dateId ->
            weightDao.updateWeightMeasurement(WeightMeasurementMapper.fromDomain(weight, dateId))
        }
    }
}
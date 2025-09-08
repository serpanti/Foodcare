package ru.foodcare.foodcare.data.weightMeasurement

import ru.foodcare.foodcare.data.date.DateMapper
import ru.foodcare.foodcare.domain.weight.Weight

object WeightMeasurementMapper {
    fun toDomain(dateWithWeightMeasurements: DateWithWeightMeasurements): List<Weight> {
        return dateWithWeightMeasurements.weightMeasurements.map { weightMeasurement ->
            Weight(
                id = weightMeasurement.id,
                value = weightMeasurement.weight,
                date = DateMapper.toDomain(dateWithWeightMeasurements.date,
                    weightMeasurement.hours,
                    weightMeasurement.minutes,
                    weightMeasurement.seconds)
            )
        }
    }

    fun fromDomain(weight: Weight, dateId: Int): WeightMeasurement = WeightMeasurement(
        id = weight.id,
        dateId = dateId,
        hours = weight.date.hour,
        minutes = weight.date.minute,
        seconds = weight.date.second,
        weight = weight.value,
    )
}
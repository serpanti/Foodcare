package ru.foodcare.foodcare.presentation.model

import ru.foodcare.foodcare.domain.weight.Weight
import java.time.LocalDateTime

data class WeightItem(val weight: Weight) : DatedItem {
    override val date: LocalDateTime = weight.date
}

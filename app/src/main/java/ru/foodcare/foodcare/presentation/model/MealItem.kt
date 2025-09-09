package ru.foodcare.foodcare.presentation.model

import ru.foodcare.foodcare.domain.meal.Meal
import java.time.LocalDateTime

data class MealItem(val meal: Meal) : DatedItem {
    override val date: LocalDateTime = meal.date
}

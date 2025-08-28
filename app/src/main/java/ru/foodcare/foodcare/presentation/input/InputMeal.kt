package ru.foodcare.foodcare.presentation.input

import ru.foodcare.foodcare.domain.meal.Meal
import ru.foodcare.foodcare.domain.product.Product
import java.lang.Exception
import java.time.LocalDateTime

class InputMeal(val id: Int,
                val product: Product?,
                val year: Int? = null,
                val month: Int? = null,
                val day: Int? = null,
                val hours: Int? = null,
                val minutes: Int? = null,
                val seconds: Int? = null) : Input
{
    override fun isCorrect(): Boolean {
        return if (year != null && month != null && day != null &&
                hours != null && minutes != null && seconds != null
                && product != null) {
            try {
                LocalDateTime.of(year, month, day, hours, minutes, seconds)
                true
            } catch (_: Exception) {
                false
            }
        } else false
    }

    fun toMeal(): Meal {
        return Meal(id, product ?: Product(
            name = "",
            production = "",
            amount = 1,
            type = Product.Companion.UnitType.Piece,
            calories = 0.0,
            protein = 0.0,
            fat = 0.0,
            carbohydrates = 0.0,
            fiber = 0.0
        ),
            year ?: 0, month ?: 0, day ?: 0,
            hours ?: 0, minutes ?: 0, seconds ?: 0
        )
    }
}
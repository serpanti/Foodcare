package ru.foodcare.foodcare.domain.meal

import ru.foodcare.foodcare.domain.product.Product
import java.time.LocalDate

data class Meal (
    val id: Int,
    val product: Product,
    val productRatio: Double,
    val date: LocalDate,
    val hours: Int,
    val minutes: Int,
    val seconds: Int
)
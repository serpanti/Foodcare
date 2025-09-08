package ru.foodcare.foodcare.domain.meal

import ru.foodcare.foodcare.domain.product.Product
import java.time.LocalDateTime

data class Meal (
    val id: Int,
    val product: Product,
    val productRatio: Double,
    val date: LocalDateTime
)
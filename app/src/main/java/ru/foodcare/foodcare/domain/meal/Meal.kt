package ru.foodcare.foodcare.domain.meal

import ru.foodcare.foodcare.domain.product.Product

data class Meal (
    val id: Int,
    val product: Product,
    val year: Int,
    val month: Int,
    val day: Int,
    val hours: Int,
    val minutes: Int,
    val seconds: Int
)
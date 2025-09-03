package ru.foodcare.foodcare.domain.weight

import java.time.LocalDateTime

data class Weight (
    val id: Int,
    val value: Double,
    val date: LocalDateTime
)
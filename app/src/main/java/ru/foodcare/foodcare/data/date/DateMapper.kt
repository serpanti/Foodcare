package ru.foodcare.foodcare.data.date

import java.time.LocalDate
import java.time.LocalDateTime

object DateMapper {
    fun fromDomain(date: LocalDate): Date = Date(
        id = 0,
        year = date.year,
        month = date.monthValue,
        day = date.dayOfMonth,
    )

    fun toDomain(date: Date, hour: Int, minute: Int, second: Int): LocalDateTime = LocalDateTime.of(
        date.year,
        date.month,
        date.day,
        hour,
        minute,
        second
    )
}
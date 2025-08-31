package ru.foodcare.foodcare.data.date

import java.time.LocalDate

object DateMapper {
    fun fromDomain(date: LocalDate): Date = Date(
        id = 0,
        year = date.year,
        month = date.monthValue,
        day = date.dayOfMonth,
    )

    fun toDomain(date: Date): LocalDate = LocalDate.of(
        date.year,
        date.month,
        date.day,
    )
}
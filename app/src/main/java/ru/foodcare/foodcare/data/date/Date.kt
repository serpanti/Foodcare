package ru.foodcare.foodcare.data.date

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity("dates",
    indices = [Index(value = ["year", "month", "day"], unique = true)])
class Date (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id: Int,

    @ColumnInfo("year")
    val year: Int,

    @ColumnInfo("month")
    val month: Int,

    @ColumnInfo("day")
    val day: Int,
)
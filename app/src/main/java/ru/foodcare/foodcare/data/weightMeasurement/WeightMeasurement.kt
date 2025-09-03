package ru.foodcare.foodcare.data.weightMeasurement

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ru.foodcare.foodcare.data.date.Date

@Entity(tableName = "weightMeasurements",
    foreignKeys = [ForeignKey(entity = Date::class, parentColumns = ["id"],
        childColumns = ["dateId"])])
data class WeightMeasurement (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id: Int = 0,

    @ColumnInfo("dateId")
    val dateId: Int,

    @ColumnInfo("hours")
    val hours: Int,

    @ColumnInfo("minutes")
    val minutes: Int,

    @ColumnInfo("seconds")
    val seconds: Int,

    @ColumnInfo("weight")
    val weight: Double = 0.0
)
package ru.foodcare.foodcare.data.product

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "products",
    indices = [Index(value = ["name", "production"], unique = true)]
)
data class Product (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "name")
    val name: String = "",

    @ColumnInfo(name = "production")
    val production: String = "",

    @ColumnInfo(name = "amount")
    val amount: Int = 1,

    @ColumnInfo(name = "type")
    val type: String = "шт",

    @ColumnInfo(name = "calories")
    val calories: Double = 0.0,

    @ColumnInfo(name = "protein")
    val protein: Double = 0.0,

    @ColumnInfo(name = "fat")
    val fat: Double = 0.0,

    @ColumnInfo(name = "carbohydrates")
    val carbohydrates: Double = 0.0,

    @ColumnInfo(name = "fiber")
    val fiber: Double = 0.0
)
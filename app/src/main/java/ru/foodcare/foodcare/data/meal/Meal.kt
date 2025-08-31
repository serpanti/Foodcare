package ru.foodcare.foodcare.data.meal

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import ru.foodcare.foodcare.data.date.Date
import ru.foodcare.foodcare.data.product.Product

@Entity("meals", foreignKeys = [
    ForeignKey(Product::class, parentColumns = ["id"],
        childColumns = ["productId"], onDelete = CASCADE),
    ForeignKey(Date::class, parentColumns = ["id"],
        childColumns = ["dateId"], onDelete = CASCADE)])
data class Meal (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id: Int,

    @ColumnInfo("productId")
    val productId: Int,

    @ColumnInfo("productRatio")
    val productRatio: Double,

    @ColumnInfo("dateId")
    val dateId: Int,

    @ColumnInfo("hours")
    val hours: Int,

    @ColumnInfo("minutes")
    val minutes: Int,

    @ColumnInfo("seconds")
    val seconds: Int
)
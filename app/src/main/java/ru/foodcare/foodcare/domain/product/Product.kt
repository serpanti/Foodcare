package ru.foodcare.foodcare.domain.product

import android.os.Parcel
import android.os.Parcelable
import ru.foodcare.foodcare.domain.nutrientsProperties.NutrientsProperties

data class Product(val name: String, val production: String,
                   val amount: Int,
                   val type: UnitType,
                   val nutrientsProperties: NutrientsProperties) : Parcelable {
    override fun describeContents(): Int = 0

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(production)
        parcel.writeInt(amount)
        parcel.writeInt(type.ordinal)
        parcel.writeDouble(nutrientsProperties.calories)
        parcel.writeDouble(nutrientsProperties.protein)
        parcel.writeDouble(nutrientsProperties.fat)
        parcel.writeDouble(nutrientsProperties.carbohydrates)
        parcel.writeDouble(nutrientsProperties.fiber)

    }

    companion object {
        enum class UnitType {
            Milliliter,
            Gram,
            Piece
        }

        @JvmField
        val CREATOR = object : Parcelable.Creator<Product> {
            override fun createFromParcel(parcel: Parcel): Product {
                val name = parcel.readString() ?: ""
                val production = parcel.readString() ?: ""
                val amount = parcel.readInt()
                val typeOrdinal = parcel.readInt()
                val type = UnitType.entries[typeOrdinal]

                val calories = parcel.readDouble()
                val protein = parcel.readDouble()
                val fat = parcel.readDouble()
                val carbohydrates = parcel.readDouble()
                val fiber = parcel.readDouble()

                val nutrientsProperties =
                    NutrientsProperties(calories, protein, fat, carbohydrates, fiber)

                return Product(name, production, amount, type, nutrientsProperties)
            }

            override fun newArray(size: Int): Array<Product?> {
                return arrayOfNulls(size)
            }
        }
    }
}
package ru.foodcare.foodcare.domain

data class Product(val name: String, val production: String,
                   val calories: Double,
                   val protein: Double,
                   val carbohydrates: Double,
                   val fat: Double,
                   val insolubleFiber: Double,
                   val solubleFiber: Double,
                   val amount: Int,
                   val type: UnitType) {

    companion object {
        enum class UnitType {
            Milliliter,
            Gram,
            Piece
        }
    }
}



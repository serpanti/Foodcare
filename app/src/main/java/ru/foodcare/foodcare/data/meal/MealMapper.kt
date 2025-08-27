package ru.foodcare.foodcare.data.meal

import ru.foodcare.foodcare.data.product.ProductMapper
import ru.foodcare.foodcare.domain.meal.Meal as MealDomain

object MealMapper {
    fun toDomain(entity: MealWithProduct) = MealDomain(
        id = entity.meal.id,
        product = ProductMapper.toDomain(entity.product),
        year = entity.meal.year,
        month = entity.meal.month,
        day = entity.meal.day,
        hours = entity.meal.hours,
        minutes = entity.meal.minutes,
        seconds = entity.meal.seconds
    )

    fun fromDomain(domain: MealDomain, productId: Int): Meal = Meal(
        id = domain.id,
        productId = productId,
        year = domain.year,
        month = domain.month,
        day = domain.day,
        hours = domain.hours,
        minutes = domain.minutes,
        seconds = domain.seconds
    )
}
package ru.foodcare.foodcare.data.meal

import ru.foodcare.foodcare.data.date.DateMapper
import ru.foodcare.foodcare.data.product.ProductMapper
import ru.foodcare.foodcare.domain.meal.Meal as MealDomain

object MealMapper {
    fun toDomain(entity: MealWithProductAndDate) = MealDomain(
        id = entity.meal.id,
        product = ProductMapper.toDomain(entity.product),
        date = DateMapper.toDomain(entity.date,
            entity.meal.hours, entity.meal.minutes, entity.meal.seconds),
        productRatio = entity.meal.productRatio
    )

    fun fromDomain(domain: MealDomain, productId: Int, dateId: Int): Meal = Meal(
        id = domain.id,
        productId = productId,
        dateId = dateId,
        hours = domain.date.hour,
        minutes = domain.date.minute,
        seconds = domain.date.second,
        productRatio = domain.productRatio
    )
}
package ru.foodcare.foodcare.ui.input

import ru.foodcare.foodcare.domain.weight.Weight
import java.lang.Exception
import java.time.LocalDateTime

class InputWeight(val id: Int,
                  val value: Double? = null,
                  val year: Int? = null,
                  val month: Int? = null,
                  val day: Int? = null,
                  val hours: Int? = null,
                  val minutes: Int? = null,
                  val seconds: Int? = null) : Input
{
    override fun isCorrect(): Boolean {
        return if (value != null && year != null && month != null && day != null &&
            hours != null && minutes != null && seconds != null) {
            try {
                LocalDateTime.of(year, month, day, hours, minutes, seconds)
                true
            } catch (_: Exception) {
                false
            }
        } else false
    }

    fun toWeight(): Weight {
        return Weight(id,
            value ?: 0.0,
            LocalDateTime.of(year ?: 0, month ?: 0, day ?: 0,
                hours ?: 0, minutes ?: 0, seconds ?: 0)
        )
    }
}
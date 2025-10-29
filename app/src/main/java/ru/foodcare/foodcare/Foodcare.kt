package ru.foodcare.foodcare

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import ru.foodcare.foodcare.di.components.DaggerFoodcareComponent
import ru.foodcare.foodcare.di.components.FoodcareComponent
import kotlin.system.exitProcess

class Foodcare: Application() {
    lateinit var foodcareComponent: FoodcareComponent
        private set

    override fun onCreate() {
        super.onCreate()
        foodcareComponent = DaggerFoodcareComponent.factory().create(this)
    }

    fun restart() {
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.set(
            AlarmManager.RTC,
            System.currentTimeMillis() + 100,
            pendingIntent
        )

        exitProcess(0)
    }
}

val Context.foodcare
    get() = applicationContext as Foodcare

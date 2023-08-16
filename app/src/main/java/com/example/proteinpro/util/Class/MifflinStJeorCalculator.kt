package com.example.proteinpro.util.Class


import java.util.Calendar
import kotlin.math.pow
import kotlin.math.round

class MifflinStJeorCalculator(
    private val gender: String,
    private val userBirth: String,
    private val heightCm: Int,
    private val weightKg: Int,
    private val activityLevel: ActivityLevel
) {
    private val carbRatio = 0.5
    private val proteinRatio = 0.3
    private val fatRatio = 0.2

    enum class ActivityLevel(val value: Double) {
        SEDENTARY(1.2),
        LIGHTLY_ACTIVE(1.375),
        MODERATELY_ACTIVE(1.55),
        VERY_ACTIVE(1.725),
        EXTRA_ACTIVE(1.9)
    }

    private fun calculateAgeFromBirth(): Int {
        val birthYear = userBirth.substringBefore("-").toInt()
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        return currentYear - birthYear
    }

    fun calculateRecommendedCalories(): Double {
        val age = calculateAgeFromBirth()

        val bmr = if (gender == "male") {
            10 * weightKg + 6.25 * heightCm - 5 * age + 5
        } else {
            10 * weightKg + 6.25 * heightCm - 5 * age - 161
        }

        return bmr * activityLevel.value
    }

    fun calculateRecommendedNutrients(): Triple<Int, Int, Int> {
        val recommendedCalories = calculateRecommendedCalories()

        val carbIntake = (recommendedCalories * carbRatio / 4.0).toInt()
        val proteinIntake = (recommendedCalories * proteinRatio / 4.0).toInt()
        val fatIntake = (recommendedCalories * fatRatio / 9.0).toInt()

        return Triple(carbIntake, proteinIntake, fatIntake)
    }

    fun calculateBMI(): Double {
        val bmi = weightKg / (heightCm / 100.0).pow(2.0)
        return round(bmi * 10) / 10
    }

    fun classifyBMI(): String {
        val bmi = calculateBMI()
        return when {
            bmi <= 18.5 -> "저체중"
            bmi <= 22.9 -> "정상"
            bmi <= 24.9 -> "과체중"
            bmi <= 29.9 -> "비만"
            else -> "고도비만"
        }
    }
}
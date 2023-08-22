package com.example.proteinpro.util.Class.food

data class NutritionFacts(// 영양성분

    val servingSize: Double,// 1회 제공량(단위는 용량단위를 사용)
    val sodium: Double, // 나트륨 mg
    val sugars: Double, // 당류
    val transFat: Double, // 트랜스지방
    val cholesterol: Double, // 콜레스테롤 mg
    val calories: Double, // 칼로리
    val carbohydrates: Double,//탄수화물
    val fat: Double,// 지방
    val saturatedFat: Double,// 포화지방
    val protein: Double// 단백질

)

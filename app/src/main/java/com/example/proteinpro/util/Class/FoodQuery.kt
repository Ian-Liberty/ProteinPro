package com.example.proteinpro.util.Class

data class FoodQuery(

    val category: String = "",
    val query: String = "",
    val skip: Int = 0,
    val limit: Int = 10,
    val kind: String? = null,
    val etc: String? = null,
    val allergy: String? = null,
    val taste: String? = null,
    val all: String? = null,
    val order: Int = 0

)

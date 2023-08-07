package com.example.proteinpro.util.Retrofit

import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface FoodDataInterface {

    data class mainRequest(
        val order: Int
    )

    @GET("food/main/list")
    fun getMainFoodlist(@Query("order") order: Int): Call<JsonElement?>?

}
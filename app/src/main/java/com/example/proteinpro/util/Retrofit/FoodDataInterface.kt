package com.example.proteinpro.util.Retrofit

import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface FoodDataInterface {



    data class mainRequest(
        val order: Int
    )

    @GET("food/main/list")
    fun getMainFoodlist(@Query("order") order: Int): Call<JsonElement?>?

    @GET("food/list/{category}")
    fun getCategoryResult(@Path("category") category: String): Call<JsonElement?>?

    @GET("food/{category}/search")
    suspend fun getSearchList(

        @Path("category") category: String,
        @Query("query") query: String,
        @Query("kind") kind: String?,
        @Query("etc") etc: String?,
        @Query("allergy") allergy: String?,
        @Query("taste") taste: String?,
        @Query("all") all: String?,
        @Query("skip") skip: Int,
        @Query("limit") limit: Int,
        @Query("order") order: Int

    ): Call<JsonElement?>?

}
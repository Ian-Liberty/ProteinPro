package com.example.proteinpro.util.Retrofit

import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface FoodDataInterface {



    data class mainRequest(
        val order: Int
    )

    data class FoodSearchRequest(
        val category: String,
        val query: String,
        val kind: String? = null,
        val etc: String? = null,
        val allergy: String? = null,
        val taste: String? = null,
        val all: String? = null,
        val skip: Int = 0,
        val limit: Int = 10,
        val order: Int = 0
    ){
        fun toMap(): Map<String, Any> {
            val map = mutableMapOf<String, Any>(
                "category" to category,
                "query" to query,
                "skip" to skip,
                "limit" to limit,
                "order" to order
            )

            if (kind != null) map["kind"] = kind
            if (etc != null) map["etc"] = etc
            if (allergy != null) map["allergy"] = allergy
            if (taste != null) map["taste"] = taste
            if (all != null) map["all"] = all

            return map
        }
    }

    @GET("food/main/list")
    fun getMainFoodlist(@Query("order") order: Int): Call<JsonElement?>?

    @GET("food/list/{category}")
    fun getCategoryResult(  @Path("category") category: String,
                            @Query("kind") kind: String?,
                            @Query("etc") etc: String?,
                            @Query("allergy") allergy: String?,
                            @Query("taste") taste: String?,
                            @Query("all") all: String?,
                            @Query("skip") skip: Int,
                            @Query("limit") limit: Int,
                            @Query("order") order: Int): Call<JsonElement?>?

    @GET("food/{category}/search")
    fun getSearchList(

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


    // 제품단일조회
    @GET("food/{food_id}")
    fun getFoodData(

        @Path("food_id") category: String

    ): Call<JsonElement?>?


}
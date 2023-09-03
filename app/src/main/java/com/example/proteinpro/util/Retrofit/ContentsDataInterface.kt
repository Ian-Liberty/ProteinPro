package com.example.proteinpro.util.Retrofit

import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ContentsDataInterface {

    @GET("board/api")
    fun getHomeFoodList(

    ): Call<JsonElement?>?

}
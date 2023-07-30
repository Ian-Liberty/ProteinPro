package com.example.proteinpro.user.util.Retrofit

import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserDataInterface {

    // 요청 클래스 생성
    data class LoginRequest(
        val 이메일: String,
        val 비밀번호: String
    )

    @POST("user/login")
    fun login(@Body request: LoginRequest): Call<JsonElement?>?


}
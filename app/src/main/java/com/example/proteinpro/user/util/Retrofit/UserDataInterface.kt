package com.example.proteinpro.user.util.Retrofit

import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface UserDataInterface {

    // 요청 클래스 생성
    /***
     * 로그인 요청
     */
    data class LoginRequest(
        val 이메일: String,
        val 비밀번호: String
    )
    data class 이메일기본(
        val 이메일: String
    )

    data class 인증번호정보(
        val 토큰: String,
        val 입력한인증번호: String
    )

    data class 닉네임기본(
        val 닉네임: String,
    )

    data class 모바일회원가입(
        val 닉네임: String,
        val 이메일: String,
        val 비밀번호: String
    )

    data class 회원가입기본(
        val 닉네임: String,
        val 이메일: String,
        val 비밀번호: String,
        val 성별: Int,
        val 생년월일: String,
        val 몸무게: Int,
        val 신장: Int,
        val 활동량: Int,
        val 등급: Int
    )

    data class 회원정보변경기본(
        val 닉네임: String,
        val 이메일: String,
        val 성별: Int,
        val 생년월일: String,
        val 몸무게: Int,
        val 신장: Int,
        val 활동량: Int
    )

    /***
     * 요청 포맷
     */
    @POST("user/login")
    fun login(@Body request: LoginRequest): Call<JsonElement?>?

    /***
     * 이메일 중복 체크 요청
     */

    @POST("user/email")
    fun checkEmailDuplication(@Body request: 이메일기본) : Call<JsonElement?>?

    @POST("user/send/number")
    fun 인증번호전송(@Body request: 이메일기본) : Call<JsonElement?>?

    @POST("user/authnum")
    fun 인증번호체크(@Body request: 인증번호정보) : Call<JsonElement?>?

    @POST("user/nickname")
    fun 닉네임중복체크(@Body request: 닉네임기본) : Call<JsonElement?>?

    @POST("user/signup")
    fun 회원가입(@Body request: 회원가입기본) : Call<JsonElement?>?

    @PUT("user/info")
    fun 사용자정보변경하기(@Body request: 회원정보변경기본) : Call<JsonElement?>?

}
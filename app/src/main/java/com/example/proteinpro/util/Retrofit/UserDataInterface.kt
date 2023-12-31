package com.example.proteinpro.util.Retrofit

import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
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

    data class 로그인기본(
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

    data class 비밀번호재설정기본(
        val 이메일: String,
        val 새비밀번호:String
    )

    data class 비밀번호변경기본(
        val 현비밀번호: String,
        val 새비밀번호:String
    )

    data class 회원탈퇴기본(
        val 비밀번호:String

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
        val 등급: Int,
        val 탈퇴: Int,
        val 타입: Int
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

    data class 토큰기본(
        val 토큰: String
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
    fun 사용자정보변경하기(@Body request: 회원정보변경기본, @Header("Authorization") authorization: String?) : Call<JsonElement?>?

    @POST("user/reset")
    fun 비밀번호재설정(@Body request: 비밀번호재설정기본, @Header("Authorization") authorization: String?) : Call<JsonElement?>?

    @POST("user/change")
    fun 비밀번호변경하기(@Body request: 비밀번호변경기본, @Header("Authorization") authorization: String?) : Call<JsonElement?>?

//    @POST("user/token")
//    fun 사용자토큰체크(@Body request: 토큰기본) : Call<JsonElement?>?

    @POST("user/token")
    fun 사용자토큰체크(@Header("Authorization") authorization: String?) : Call<JsonElement?>?

    @POST("user/info")
    fun 사용자정보불러오기(@Header("Authorization") authorization: String?) : Call<JsonElement?>?

    @POST("user/withdraw")
    fun 회원탈퇴(@Body request: 회원탈퇴기본, @Header("Authorization") authorization: String?) : Call<JsonElement?>?


    @POST("user/wallet")
    fun 지갑정보가져오기(@Header("Authorization") authorization: String?) : Call<JsonElement?>?


}
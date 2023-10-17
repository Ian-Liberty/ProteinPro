package com.example.proteinpro.util.Retrofit

import com.google.gson.JsonElement
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface FoodDataInterface {
    data class ReviewRequest(
        val good: String,
        val bad: String,
        val star: Int
    )

    data class 식품키(
        val 식품키: Int
    )

    data class 후기변경기본(

       val 타입 : String,
       val 후기키 : String

    )

    data class 후기기본(

       @Field("good") val 긍정후기 : String,
       @Field("bad") val 부정후기 : String,
       @Field("star") val 평점 : Int

    )

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
    fun getCategoryResult(
        @Path("category") category: String,
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
    @GET("food/m/{food_id}")
    fun getFoodData(

        @Path("food_id") category: String,
        @Header("Authorization") authorization: String

    ): Call<JsonElement?>?

    @GET("food/home/list/")
    fun getHomeFoodList(

        @Query("타입") type: String

    ): Call<JsonElement?>?

    //리뷰 등록
    @Multipart
    @POST("review/post/{food_id}")
    fun submitReview(

        @Path("food_id") foodId: String,
        @Header("Authorization") authorization: String,
        @Part("good") good: RequestBody,
        @Part("bad") bad: RequestBody,
        @Part("star") star: Int,
        @Part files: List<MultipartBody.Part>

    ): Call<JsonElement?>?

    //리뷰 리스트
    @GET("review/list/{food_id}")
    fun getReviewList(

        @Path("food_id") category: String,
        @Header("Authorization") authorization: String,
        @Query("skip") skip: Int,
        @Query("limit") limit: Int,
        @Query("order") order: Int

    ): Call<JsonElement?>?

    @Multipart
    @PUT("review/{review_id}")
    fun updateReview(
        @Path("review_id") reviewId: String,
        @Header("Authorization") authorization: String,
        @Part("good") good: RequestBody,
        @Part("bad") bad: RequestBody,
        @Part("star") star: Int,
        @Part("img") img: List<String>,
        @Part("isChanged") isChanged: Boolean,
        @Part images: List<MultipartBody.Part>
    ): Call<JsonElement>

    @GET("review/m/{review_id}")
    fun getReviewDetails(
        @Path("review_id") reviewId: String,
        @Header("Authorization") authorization: String
    ): Call<JsonElement>

    @GET("review/m/list")
    fun myReviewList(

        @Header("Authorization") authorization: String,
        @Query("order") order : Int,
        @Query("skip") skip : Int,
        @Query("limit") limit : Int

    ): Call<JsonElement>

    @DELETE("review/del/{review_id}")
    fun deleteReview(
        @Path("review_id") reviewId: Int,
        @Header("Authorization") authorization: String
    ): Call<Void>
    @POST("review/like")
    fun addLike(
        @Body request : 후기변경기본,
        @Header("Authorization") authorization: String
    ): Call<Void>

    @DELETE("review/like/{review_id}")
    fun deleteLike(
        @Path("review_id") reviewId: Int,
        @Header("Authorization") authorization: String
    ): Call<Void>

    @POST("food/fav/")//관심상품 추가
    fun addFavFood(
        @Body 식품키: Int,
        @Header("Authorization") authorization: String
    ): Call<JsonElement>

    @DELETE("food/fav/m/del/{food_id}")
    fun delFavFood(
        @Path("food_id") food_id: Int,
        @Header("Authorization") authorization: String
    ): Call<JsonElement>

    /***
     * TODO
     * 백엔드 상태 확인후 고쳐둘것
     */
    @GET("food/fav/list")
    fun myFavFoodList(

        @Header("Authorization") authorization: String,
        @Query("skip") skip : Int


    ): Call<JsonElement>
}




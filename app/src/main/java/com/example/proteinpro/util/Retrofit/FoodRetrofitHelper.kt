package com.example.proteinpro.util.Retrofit
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import android.content.Context
import android.util.Log
import com.example.proteinpro.util.Class.FoodQuery
import com.example.proteinpro.util.Class.food.AdditiveItem
import com.example.proteinpro.util.Class.food.FoodInformationItem
import com.example.proteinpro.util.Class.food.NutritionFacts
import com.example.proteinpro.util.Class.food.ProteinDataItem
import com.example.proteinpro.util.Class.food.ReviewItem
import com.example.proteinpro.util.RecyclerView.FoodItem
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.URLDecoder

class FoodRetrofitHelper(context: Context?) {

    private val retrofit = ApiClient.getApiClient()
    private val ctx = context

    interface MainFoodListCallback {
        fun onSuccess(foodList: JsonArray)
        fun onFailure()
    }

    interface reviewListCallback {
        fun onSuccess(reviewList: JsonArray)
        fun onFailure()
    }

    interface reviewDataCallback {
        fun  onSuccess(reviewItem: ReviewItem)
        fun onFailure()
    }


    interface FoodDataCallback {
        fun onSuccess(foodData: FoodInformationItem)
        fun onFailure()
    }



    fun parseJsonResponse(jsonResponse: JsonArray): ArrayList<FoodItem> {
        val foodItemList = ArrayList<FoodItem>()
        val gson = Gson()

        for (jsonElement in jsonResponse) {
            val foodItem = gson.fromJson(jsonElement, FoodItem::class.java)
            foodItemList.add(foodItem)
        }

        return foodItemList
    }

    /***
     * 메인 페이지 food 리스트 받아오기
     */
    fun searchMainFoodList(order: Int, callback: MainFoodListCallback){// 성공 시 푸드리스트를 콜백해 줌

        val api =retrofit.create(FoodDataInterface::class.java)// 사용할 인터페이스

        val call = api.getMainFoodlist(order)

        call?.enqueue(object : Callback<JsonElement?> {

            override fun onResponse(call: Call<JsonElement?>, response: Response<JsonElement?>) {


                if (response.isSuccessful) {

                    val jsonResponse = response.body()?.asJsonObject
                    Log.i("onSuccess", response.body().toString())

                    if (jsonResponse != null) {
        //             응답에서 변수 호출    jsonResponse.get("키값").asString
                        // JSON을 파싱하여 리스트로 변환하는 작업 수행
                        if(jsonResponse.get("메세지").asString == "true"){
                            val data = jsonResponse.get("데이터").asJsonObject
                            val foodList = data.get("식품목록").asJsonArray

                            Log.i ("searchMainFoodList", ""+foodList)

                            callback.onSuccess(foodList)
                        }else{
                            Log.e("onFailure", "응답이 올바르지 않음 : 메시지 값이 false 임")
                            callback.onFailure()
                        }

                    } else {
                        Log.e("onFailure", "응답이 올바르지 않음 : jsonResponse 값이 null 임")
                        callback.onFailure()

                    }
                } else {

                    Log.e("onFailure", "응답이 올바르지 않음 : response.isSuccessful 값이 false 임")
                    callback.onFailure()
                }

            }

            override fun onFailure(call: Call<JsonElement?>, t: Throwable) {
                Log.i("onFailure", t.toString())
                callback.onFailure()
            }
        })


    }


    /***
     * 유저의 검색어 혹은 카테고리에
     * 받아야할 정보
     *
     */
    fun getSearchFoodList(foodQuery: FoodQuery, callback: MainFoodListCallback){
        Log.i ("getSearchFoodList", "")

        val retrofit = ApiClient.getApiClient()
        val api =retrofit.create(FoodDataInterface::class.java)// 사용할 인터페이스

        val category = foodQuery.category
        val query = URLDecoder.decode(foodQuery.query, "UTF-8")
        val kind =foodQuery. kind
        val etc = foodQuery.etc
        val allergy = foodQuery.allergy
        val taste = foodQuery.taste
        val all = foodQuery. all
        val skip =foodQuery. skip
        val limit = foodQuery.limit
        val order = foodQuery.order

        Log.i ("정보태그", "query: "+ query)

        val call = api.getSearchList(category, query, kind, etc, allergy, taste, all, skip, limit, order )

        call?.enqueue(object : Callback<JsonElement?> {

            override fun onResponse(call: Call<JsonElement?>, response: Response<JsonElement?>) {


                if (response.isSuccessful) {
                    val jsonResponse = response.body()?.asJsonObject
                    Log.i("onSuccess", response.body().toString())

                    if (jsonResponse != null) {
                        if(jsonResponse.get("메세지").asString == "true"){
                            val data = jsonResponse.get("데이터").asJsonObject
                            val data_more = data.get("식품리스트").asJsonObject

                            val foodList = data_more.get("식품목록").asJsonArray

                            Log.i ("searchMainFoodList", ""+foodList)

                            callback.onSuccess(foodList)
                        }else{

                            Log.e("onFailure", "응답이 올바르지 않음 : 메시지 값이 false 임")
                            callback.onFailure()
                        }
                    }else{

                        Log.e("onFailure", "응답이 올바르지 않음 : jsonResponse 값이 null 임")
                        callback.onFailure()
                    }

                } else {
                    Log.e("onFailure", "응답이 올바르지 않음 : response.isSuccessful 값이 false 임")
                    callback.onFailure()
                }

            }

            override fun onFailure(call: Call<JsonElement?>, t: Throwable) {
                Log.i("onFailure", t.toString())
                callback.onFailure()
            }
        })

    }

    fun getCategoryResult(foodQuery: FoodQuery , callback: MainFoodListCallback){
        Log.i ("정보태그", "categoryKey: $foodQuery")

        val api =retrofit.create(FoodDataInterface::class.java)// 사용할 인터페이스

        val category = foodQuery.category
        val kind =foodQuery. kind
        val etc = foodQuery.etc
        val allergy = foodQuery.allergy
        val taste = foodQuery.taste
        val all = foodQuery. all
        val skip =foodQuery. skip
        val limit = foodQuery.limit
        val order = foodQuery.order

        val call = api.getCategoryResult(category, kind, etc, allergy, taste, all, skip, limit, order )

        call?.enqueue(object : Callback<JsonElement?> {

            override fun onResponse(call: Call<JsonElement?>, response: Response<JsonElement?>) {


                if (response.isSuccessful) {
                    val jsonResponse = response.body()?.asJsonObject
                    Log.i("onSuccess", response.body().toString())

                    if (jsonResponse != null) {
                        //             응답에서 변수 호출    jsonResponse.get("키값").asString
                        // JSON을 파싱하여 리스트로 변환하는 작업 수행
                        if(jsonResponse.get("메세지").asString == "true"){
                            val data = jsonResponse.get("데이터").asJsonObject
                            val more_data = data.get("식품리스트").asJsonObject

                            val foodList = more_data.get("식품목록").asJsonArray

                            Log.i ("searchMainFoodList", ""+foodList)

                            callback.onSuccess(foodList)
                        }else{
                            Log.e("onFailure", "응답이 올바르지 않음 : 메시지 값이 false 임")
                            callback.onFailure()
                        }
                    }
                } else {
                    Log.e("onFailure", "응답이 올바르지 않음 : response.isSuccessful 값이 false 임")
                        callback.onFailure()
                }

            }

            override fun onFailure(call: Call<JsonElement?>, t: Throwable) {
                Log.i("onFailure", t.toString())
                callback.onFailure()
            }
        })

    }

    fun getFoodData(food_key: String,token: String?, callback: FoodDataCallback){

        val retrofit = ApiClient.getApiClient()
        val api =retrofit.create(FoodDataInterface::class.java)// 사용할 인터페이스

        // 로그인의 경우 UserDataInterface.LoginRequest(email, password)

        val call = api.getFoodData(food_key, "Bearer "+token)

        call?.enqueue(object : Callback<JsonElement?> {

            override fun onResponse(call: Call<JsonElement?>, response: Response<JsonElement?>) {


                if (response.isSuccessful) {
                    val jsonResponse = response.body()?.asJsonObject
                    Log.i("onSuccess", response.body().toString())

                    if (jsonResponse != null) {
                        //             응답에서 변수 호출    jsonResponse.get("키값").asString
                        // JSON을 파싱하여 리스트로 변환하는 작업 수행
                        if(jsonResponse.get("메세지").asString == "true"){

                            val gson = Gson()

                            val data = jsonResponse.get("데이터").asJsonObject

                            //데이터 파싱
                            val foodData = data.get("식품").asJsonObject// 식품 데이터
                            val nutrient = data.get("영양성분").asJsonObject// 영양성분
                            val foodlike = data.get("관심식품").asJsonObject// 관심정보
                            val ProteinNames = data.get("단백질명").asJsonArray// 단백질 명 리스트
                            val additivesJsonArray  = data.get("첨가물리스트").asJsonArray // 첨가물 리스트
                            val aiReviewData = data.get("ai후기").asJsonObject
                            val tagJsonArray = data.get("태그").asJsonArray
                            val porteinDataJsonArray = data.get("단백질종류").asJsonArray

                            // 후기
                            val grade = getFloatFromJson(data,"후기평균점수")
                            val numberOfReview = getIntFromJson(foodData,"총후기수")
                            val review  = data.get("후기목록").asJsonObject
                            var reviewList: ArrayList<ReviewItem> = ArrayList()


                            if((review.get("후기목록").asJsonArray.isJsonNull)){

                            }else{
                                reviewList = gson.fromJson<ArrayList<ReviewItem>>(review.get("후기목록").asJsonArray, object : TypeToken<ArrayList<ReviewItem>>() {}.type)
                            }

                            //foodData
                            val key = foodData.get("키").asString
                            val name = foodData.get("이름").asString
                            val tasteElement = foodData.get("맛")
                            val taste = if (!tasteElement.isJsonNull) {
                                tasteElement.asString
                            } else {
                                ""
                            }
                            val price = foodData.get("가격").asInt
                            val capacity = foodData.get("용량").asDouble
                            val capacityUnit = foodData.get("단위").asString
                            val image = foodData.get("이미지").asString
                            val link = foodData.get("판매링크").asString
                            val costPerformance= foodData.get("가성비").asFloat
                            val quantity = foodData.get("수량").asInt
                            val brand= foodData.get("브랜드").asString
                            val company= foodData.get("회사명").asString
                            val proteinTypeList = gson.fromJson<ArrayList<String>>(ProteinNames, object : TypeToken<ArrayList<String>>() {}.type)

                            val additiveList: ArrayList<AdditiveItem> = ArrayList()

                            // 첨가물 데이터 리스트 파싱
                            for (i in 0 until additivesJsonArray.size()) {

                                val additiveJsonArray = additivesJsonArray.get(i).asJsonObject
                                val name = additiveJsonArray.get("첨가물").asString
                                val ewgGrade = additiveJsonArray.get("등급").asInt
                                val purpose = additiveJsonArray.get("용도").asString
                                Log.i ("첨가물", "name: {name}")
                                val additiveItem = AdditiveItem(name, ewgGrade, purpose)
                                additiveList.add(additiveItem)

                            }

                            val allergtListStr= stringNullCheck(foodData.get("알레르기"))
                            val positiveReviewWords= aiReviewData.get("긍정단어").asString// 긍정단어 리스트
                            val negativeReviewWords =aiReviewData.get("부정단어").asString// 긍정단어 리스트

                            // 태그 리스트 파싱
                            val tagList : ArrayList<String> = ArrayList()// 태그 단어
                            for (i in 0 until tagJsonArray.size()) {
                                val tag = tagJsonArray.get(i).asString
                                tagList.add(tag)
                            }

                            // 영양소 데이터
                            val servingSize = nutrient.get("용량").asDouble
                            val sodium = nutrient.get("나트륨").asDouble
                            val sugars = nutrient.get("당류").asDouble
                            val transFat = nutrient.get("트랜스지방").asDouble
                            val cholesterol = nutrient.get("콜레스테롤").asDouble
                            val calories = nutrient.get("칼로리").asDouble
                            val carbohydrates = nutrient.get("탄수화물").asDouble
                            val fat = nutrient.get("지방").asDouble
                            val saturatedFat = nutrient.get("포화지방").asDouble
                            val protein = nutrient.get("단백질").asDouble

                            val nutritionFacts
                            = NutritionFacts(servingSize,sodium,sugars,transFat,cholesterol,calories,carbohydrates,fat,saturatedFat,protein)

                            val proteinList: ArrayList<ProteinDataItem> = ArrayList()// 설명이 포함된 단백질 목록
                            // 단백질 리스트 파싱
                            for (i in 0 until porteinDataJsonArray.size()) {

                                val proteinData = porteinDataJsonArray.get(i).asJsonObject
                                val name = proteinData.get("성분명").asString
                                val explanation = proteinData.get("설명").asString
                                Log.i ("첨가물", "name: {name}")
                                val proteinDataItem = ProteinDataItem(name, explanation)
                                proteinList.add(proteinDataItem)
                            }

                            val rowMaterials= foodData.get("원재료").asString
                            val originMaterials = foodData.get("원본").asString
                            //

                            val isLike = foodlike.get("등록여부").asInt
                            //0이면 미등록, 1이면 관심상품
                            val likeCount = foodlike.get("관심갯수").asInt

                            val foodInformationItem =
                                FoodInformationItem(
                                    key,
                                    name,
                                    taste,
                                    price,
                                    capacity,
                                    capacityUnit,
                                    image,
                                    costPerformance,
                                    quantity,
                                    brand,
                                    company,
                                    proteinTypeList,
                                    additiveList,
                                    allergtListStr,
                                    positiveReviewWords,
                                    negativeReviewWords,
                                    tagList,
                                    nutritionFacts,
                                    proteinList,
                                    rowMaterials,
                                    originMaterials,
                                    link,
                                    grade,
                                    isLike,
                                    likeCount,
                                    reviewList
                                )

                            Log.i ("searchMainFoodList", ""+foodInformationItem)

                            callback.onSuccess(foodInformationItem)
                        }else{
                            Log.e("onFailure", "응답이 올바르지 않음 : 메시지 값이 false 임")
                            callback.onFailure()
                        }
                    }
                } else {
                    Log.e("onFailure", "응답이 올바르지 않음 : response.isSuccessful 값이 false 임")
                    callback.onFailure()
                }

            }

            override fun onFailure(call: Call<JsonElement?>, t: Throwable) {
                Log.i("onFailure", t.toString())
                callback.onFailure()
            }
        })

    }

    fun getHomeFoodList(type: String, callback: MainFoodListCallback){

        val api =retrofit.create(FoodDataInterface::class.java)// 사용할 인터페이스

        val call = api.getHomeFoodList(type)

        call?.enqueue(object : Callback<JsonElement?> {

            override fun onResponse(call: Call<JsonElement?>, response: Response<JsonElement?>) {

                if (response.isSuccessful) {

                    val jsonResponse = response.body()?.asJsonObject
                    Log.i("onSuccess", response.body().toString())

                    if (jsonResponse != null) {
                        //             응답에서 변수 호출    jsonResponse.get("키값").asString
                        // JSON을 파싱하여 리스트로 변환하는 작업 수행
                        if(jsonResponse.get("메세지").asString == "true"){
                            val data = jsonResponse.get("데이터").asJsonObject
                            val foodList = data.get("식품목록").asJsonArray

                            Log.i ("searchMainFoodList", ""+foodList)

                            callback.onSuccess(foodList)
                        }else{
                            Log.e("onFailure", "응답이 올바르지 않음 : 메시지 값이 false 임")
                            callback.onFailure()
                        }

                    } else {
                        Log.e("onFailure", "응답이 올바르지 않음 : jsonResponse 값이 null 임")
                        callback.onFailure()

                    }
                } else {

                    Log.e("onFailure", "응답이 올바르지 않음 : response.isSuccessful 값이 false 임")
                    callback.onFailure()
                }

            }

            override fun onFailure(call: Call<JsonElement?>, t: Throwable) {
                Log.i("onFailure", t.toString())
                callback.onFailure()
            }
        })

    }

    fun submitReview(good: String, bad: String, star: Int, food_key: String, token:String, imageFiles: List<MultipartBody.Part>, onResult: (Boolean) -> Unit){
        Log.i ("imageFiles.size", "imageFiles.size : "+imageFiles.size)


        val retrofit = ApiClient.getApiClient()
        val api =retrofit.create(FoodDataInterface::class.java)// 사용할 인터페이스
        // 로그인의 경우 UserDataInterface.LoginRequest(email, password)

        val call = api.submitReview(food_key,"Bearer "+token,good.toRequestBody(),bad.toRequestBody(),star,imageFiles )

        call?.enqueue(object : Callback<JsonElement?> {

            override fun onResponse(call: Call<JsonElement?>, response: Response<JsonElement?>) {


                if (response.isSuccessful) {
                    val jsonResponse = response.body()?.asJsonObject
                    Log.i("onSuccess", response.body().toString())

                    if (jsonResponse != null) {
        //             응답에서 변수 호출    jsonResponse.get("키값").asString

                        if(jsonResponse.get("메세지").asString == "true"){
                            onResult(true)
                        }else{
                            onResult(false)
                        }
                    } else {
                        Log.e("onFailure", "응답이 올바르지 않음 : jsonResponse 값이 null 임")
                        onResult(false)
                    }
                } else {
                    Log.e("onFailure", "응답이 올바르지 않음 : response.isSuccessful 값이 false 임")
                    onResult(false)
                }

            }

            override fun onFailure(call: Call<JsonElement?>, t: Throwable) {
                Log.i("onFailure", t.toString())
                onResult(false)
            }
        })


    }

    fun getReviewList( food_key: String,token: String,order: Int, skip:Int, limit:Int , callback: reviewListCallback){

        val retrofit = ApiClient.getApiClient()
        val api =retrofit.create(FoodDataInterface::class.java)// 사용할 인터페이스


        // 로그인의 경우 UserDataInterface.LoginRequest(email, password)

        val call = api.getReviewList(food_key, "Bearer "+token,skip, limit, order)

        call?.enqueue(object : Callback<JsonElement?> {

            override fun onResponse(call: Call<JsonElement?>, response: Response<JsonElement?>) {


                if (response.isSuccessful) {
                    val jsonResponse = response.body()?.asJsonObject
                    Log.i("onSuccess", response.body().toString())

                    if (jsonResponse != null) {
                        //             응답에서 변수 호출    jsonResponse.get("키값").asString
                        // JSON을 파싱하여 리스트로 변환하는 작업 수행
                        if(jsonResponse.get("메세지").asString == "true"){
                            val data = jsonResponse.get("데이터").asJsonObject
                            val reviewData = data.get("후기목록데이터").asJsonObject
                            val reviewList = reviewData.get("후기목록").asJsonArray

                            Log.i ("getReviewList", ""+reviewData)

                            callback.onSuccess(reviewList)
                        }else{
                            Log.e("onFailure", "응답이 올바르지 않음 : 메시지 값이 false 임")
                            callback.onFailure()
                        }
                    } else {
                        Log.e("onFailure", "응답이 올바르지 않음 : jsonResponse 값이 null 임")
                        callback.onFailure()
                    }
                } else {
                    Log.e("onFailure", "응답이 올바르지 않음 : response.isSuccessful 값이 false 임")
                    callback.onFailure()
                }

            }

            override fun onFailure(call: Call<JsonElement?>, t: Throwable) {
                Log.i("onFailure", t.toString())
                callback.onFailure()
            }
        })

    }

    fun getMyReviewList(token: String, skip: Int, callback: reviewListCallback){

        val retrofit = ApiClient.getApiClient()
        val api =retrofit.create(FoodDataInterface::class.java)// 사용할 인터페이스


        val call = api.myReviewList("Bearer "+token, 1,skip, 10)

        call?.enqueue(object : Callback<JsonElement?> {

            override fun onResponse(call: Call<JsonElement?>, response: Response<JsonElement?>) {


                if (response.isSuccessful) {
                    val jsonResponse = response.body()?.asJsonObject
                    Log.i("onSuccess", response.body().toString())

                    if (jsonResponse != null) {
        //             응답에서 변수 호출    jsonResponse.get("키값").asString

                        if(jsonResponse.get("메세지").asString == "true"){
                            val data = jsonResponse.get("데이터").asJsonObject
                            val reviewData = data.get("후기목록데이터").asJsonObject
                            val reviewList = reviewData.get("후기목록").asJsonArray

                            Log.i ("getReviewList", ""+reviewData)

                            callback.onSuccess(reviewList)
                        }else{
                            Log.e("onFailure", "응답이 올바르지 않음 : 메시지 값이 false 임")
                            callback.onFailure()
                        }
                    } else {
                        Log.e("onFailure", "응답이 올바르지 않음 : jsonResponse 값이 null 임")
                        callback.onFailure()
                    }
                } else {
                    Log.e("onFailure", "응답이 올바르지 않음 : response.isSuccessful 값이 false 임")
                    callback.onFailure()
                }

            }

            override fun onFailure(call: Call<JsonElement?>, t: Throwable) {
                Log.i("onFailure", t.toString())
                callback.onFailure()
            }
        })

    }

    fun getReviewDetails(token: String, reviewId:String , callback: reviewDataCallback){

        val retrofit = ApiClient.getApiClient()
        val api =retrofit.create(FoodDataInterface::class.java)// 사용할 인터페이스

        // 로그인의 경우 UserDataInterface.LoginRequest(email, password)

        val call = api.getReviewDetails(reviewId,"Bearer "+token)

        call?.enqueue(object : Callback<JsonElement?> {

            override fun onResponse(call: Call<JsonElement?>, response: Response<JsonElement?>) {


                if (response.isSuccessful) {
                    val jsonResponse = response.body()?.asJsonObject
                    Log.i("onSuccess", response.body().toString())

                    if (jsonResponse != null) {
                        //             응답에서 변수 호출    jsonResponse.get("키값").asString
                        // JSON을 파싱하여 리스트로 변환하는 작업 수행
                        if(jsonResponse.get("메세지").asString == "true"){
                            val gson = Gson()

                            val data = jsonResponse.get("데이터").asJsonObject
                            val reviewData = data.get("후기").asJsonObject
                            val reviewItem = gson.fromJson(reviewData, ReviewItem::class.java)

                            Log.i ("getReviewList", ""+reviewData)

                            callback.onSuccess(reviewItem)
                        }else{
                            Log.e("onFailure", "응답이 올바르지 않음 : 메시지 값이 false 임")
                            callback.onFailure()
                        }
                    } else {
                        Log.e("onFailure", "응답이 올바르지 않음 : jsonResponse 값이 null 임")
                        callback.onFailure()
                    }
                } else {
                    Log.e("onFailure", "응답이 올바르지 않음 : response.isSuccessful 값이 false 임")
                    callback.onFailure()
                }

            }

            override fun onFailure(call: Call<JsonElement?>, t: Throwable) {
                Log.i("onFailure", t.toString())
                callback.onFailure()
            }
        })

    }

    fun updateReviews(reviewItem: ReviewItem, isChange : Boolean, token: String, imageFiles: List<MultipartBody.Part>, onResult: (Boolean) -> Unit ){
        val retrofit = ApiClient.getApiClient()
        val api =retrofit.create(FoodDataInterface::class.java)// 사용할 인터페이스

        val id = reviewItem.키
        val token = "Bearer "+token
        val good = reviewItem.긍정후기.toRequestBody()
        val bad = reviewItem.부정후기.toRequestBody()
        val star = reviewItem.평점
        val img = reviewItem.이미지주소

        val call = api.updateReview(id,token,good,bad,star,img,isChange,imageFiles)

        call?.enqueue(object : Callback<JsonElement?> {

            override fun onResponse(call: Call<JsonElement?>, response: Response<JsonElement?>) {

                if (response.isSuccessful) {
                    val jsonResponse = response.body()?.asJsonObject
                    Log.i("onSuccess", response.body().toString())

                    if (jsonResponse != null) {
        //             응답에서 변수 호출    jsonResponse.get("키값").asString

                        if(jsonResponse.get("메세지").asString == "true"){
                            onResult(true)
                        }else{
                            onResult(false)
                        }

                    } else {
                        Log.e("onFailure", "응답이 올바르지 않음 : jsonResponse 값이 null 임")
                        onResult(false)
                    }
                } else {
                    Log.e("onFailure", "응답이 올바르지 않음 : response.isSuccessful 값이 false 임")
                    onResult(false)
                }

            }

            override fun onFailure(call: Call<JsonElement?>, t: Throwable) {
                Log.i("onFailure", t.toString())
                onResult(false)
            }
        })
    }

    fun deleteReview(reviewId: Int, token: String, onResult: (Boolean) -> Unit) {

        val token = "Bearer "+token

        val retrofit = ApiClient.getApiClient()
        val api =retrofit.create(FoodDataInterface::class.java)// 사용할 인터페이스


        val call = api.deleteReview(reviewId, token)

        call?.enqueue(object : Callback<Void> {

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                onResult(true)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onResult(false)
            }
        })

    }

    fun addLike(type: String, reviewKey : String, token: String, onResult: (Boolean) -> Unit){

        val token = "Bearer "+token

        val retrofit = ApiClient.getApiClient()
        val api =retrofit.create(FoodDataInterface::class.java)// 사용할 인터페이스

        val request =FoodDataInterface.후기변경기본(type,reviewKey)
        // 로그인의 경우 UserDataInterface.LoginRequest(email, password)

        val call = api.addLike(request, token)

        call?.enqueue(object : Callback<Void> {


            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Log.i("onSuccess", response.body().toString())
                onResult(true)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.i("onFailure", t.toString())
                onResult(false)
            }
        })

    }

    fun deleteLike(reviewId: Int, token: String , onResult: (Boolean) -> Unit){

        val retrofit = ApiClient.getApiClient()
        val api =retrofit.create(FoodDataInterface::class.java)// 사용할 인터페이스
        val token = "Bearer "+token

        val call = api.deleteLike(reviewId, token)

        call?.enqueue(object : Callback<Void> {

            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                Log.i("onSuccess", response.body().toString())
                onResult(true)

            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.i("onFailure", t.toString())
                onResult(false)

            }
        })


    }

    fun addFavFood(foodKey : Int, token : String, onResult: (Boolean) -> Unit){
        val token = "Bearer "+token

        val retrofit = ApiClient.getApiClient()
        val api =retrofit.create(FoodDataInterface::class.java)// 사용할 인터페이스

        val call = api.addFavFood(foodKey, token)

        call?.enqueue(object : Callback<JsonElement?> {
            override fun onResponse(call: Call<JsonElement?>, response: Response<JsonElement?>) {

                if (response.isSuccessful) {
                    val jsonResponse = response.body()?.asJsonObject
                    Log.i("onSuccess", response.body().toString())

                    if (jsonResponse != null) {
                        //응답에서 변수 호출    jsonResponse.get("키값").asString

                        if(jsonResponse.get("메세지").asString == "true"){
                            onResult(true)
                        }else{
                            onResult(false)
                        }

                    } else {
                        Log.e("onFailure", "응답이 올바르지 않음 : jsonResponse 값이 null 임")
                        onResult(false)
                    }
                } else {
                    Log.e("onFailure", "응답이 올바르지 않음 : response.isSuccessful 값이 false 임")
                    onResult(false)
                }


            }

            override fun onFailure(call: Call<JsonElement?>, t: Throwable) {
                onResult(false)
            }

        })
    }

    fun delFavFood(reviewId: Int, token: String , onResult: (Boolean) -> Unit){

        val retrofit = ApiClient.getApiClient()
        val api =retrofit.create(FoodDataInterface::class.java)// 사용할 인터페이스
        val token = "Bearer "+token

        val call = api.delFavFood(reviewId, token)

        call?.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    val jsonResponse = response.body()?.asJsonObject
                    Log.i("onSuccess", response.body().toString())

                    if (jsonResponse != null) {
                        //응답에서 변수 호출    jsonResponse.get("키값").asString

                        if(jsonResponse.get("메세지").asString == "true"){
                            onResult(true)
                        }else{
                            onResult(false)
                        }

                    } else {
                        Log.e("onFailure", "응답이 올바르지 않음 : jsonResponse 값이 null 임")
                        onResult(false)
                    }
                } else {
                    Log.e("onFailure", "응답이 올바르지 않음 : response.isSuccessful 값이 false 임")
                    onResult(false)
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                onResult(false)
            }

        })

    }

    fun getMyFavList(token: String, skip : Int,  callback: MainFoodListCallback ){

        val retrofit = ApiClient.getApiClient()
        val api =retrofit.create(FoodDataInterface::class.java)// 사용할 인터페이스

        val bToken = "Bearer "+token

        val call = api.myFavFoodList(bToken, skip)

        call?.enqueue(object : Callback<JsonElement?> {

            override fun onResponse(call: Call<JsonElement?>, response: Response<JsonElement?>) {


                if (response.isSuccessful) {
                    val jsonResponse = response.body()?.asJsonObject
                    Log.i("onSuccess", response.body().toString())

                    if (jsonResponse != null) {
        //             응답에서 변수 호출    jsonResponse.get("키값").asString
                        val data = jsonResponse.get("데이터").asJsonObject
                        val more_data = data.get("관심제품데이터").asJsonObject

                        val foodList = more_data.get("관심제품목록").asJsonArray

                        Log.i ("getMyFavList", ""+foodList)

                        callback.onSuccess(foodList)

                    } else {
                        Log.e("onFailure", "응답이 올바르지 않음 : jsonResponse 값이 null 임")
                        callback.onFailure()
                    }
                } else {
                    Log.e("onFailure", "응답이 올바르지 않음 : response.isSuccessful 값이 false 임")
                    callback.onFailure()
                }

            }

            override fun onFailure(call: Call<JsonElement?>, t: Throwable) {
                Log.i("onFailure", t.toString())
                callback.onFailure()
            }
        })

    }


    fun stringNullCheck(item: JsonElement) :  String{

      return  if (!item.isJsonNull) {
          item.asString
        } else {
            ""
        }

    }


    fun arrayNullCheck(item: JsonArray){

    }
    private fun getStringFromJson(jsonObject: JsonObject, key: String): String {
        return if (jsonObject.has(key) && !jsonObject.get(key).isJsonNull) {
            jsonObject.get(key).asString
        } else {
            ""
        }
    }

    private fun getIntFromJson(jsonObject: JsonObject, key: String): Int {
        return if (jsonObject.has(key) && !jsonObject.get(key).isJsonNull) {
            jsonObject.get(key).asInt
        } else {
            0
        }
    }

    private fun getFloatFromJson(jsonObject: JsonObject, key: String): Float {
        return if (jsonObject.has(key) && !jsonObject.get(key).isJsonNull) {
            jsonObject.get(key).asFloat
        } else {
            0.0f
        }
    }



}
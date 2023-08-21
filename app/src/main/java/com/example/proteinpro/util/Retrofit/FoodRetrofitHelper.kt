package com.example.proteinpro.util.Retrofit
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import android.content.Context
import android.util.Log
import com.example.proteinpro.util.Class.FoodQuery
import com.example.proteinpro.util.RecyclerView.FoodItem
import com.google.gson.Gson
import com.google.gson.JsonArray
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

class FoodRetrofitHelper(context: Context?) {

    private val retrofit = ApiClient.getApiClient()
    private val ctx = context

    interface MainFoodListCallback {
        fun onSuccess(foodList: JsonArray)
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
     * 기능 정의
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


}
package com.example.proteinpro.util.Retrofit
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import android.content.Context
import android.util.Log
import com.example.proteinpro.util.Class.User
import com.google.gson.JsonArray

class ContentsRetrofitHelper(context: Context?) {

    private val retrofit = ApiClient.getApiClient()
    private val ctx = context

    interface YoutubeListCallback {
        fun onSuccess(youtubelist: JsonArray)
        fun onFailure()
    }

    fun getYoutubeList(callback: YoutubeListCallback){

        val retrofit = ApiClient.getApiClient()
        val api =retrofit.create(ContentsDataInterface::class.java)// 사용할 인터페이스


        val call = api.getHomeFoodList()

        call?.enqueue(object : Callback<JsonElement?> {

            override fun onResponse(call: Call<JsonElement?>, response: Response<JsonElement?>) {


                if (response.isSuccessful) {
                    val jsonResponse = response.body()?.asJsonObject
                    Log.i("onSuccess", response.body().toString())

                    if (jsonResponse != null) {
        //             응답에서 변수 호출    jsonResponse.get("키값").asString
                        //             응답에서 변수 호출    jsonResponse.get("키값").asString
                        if(jsonResponse.get("메시지").asString == "true"){
                            val data = jsonResponse.get("데이터").asJsonObject
                            val itemList = data.get("게시글목록").asJsonArray

                            callback.onSuccess(itemList)
                        }else{

                            Log.i ("정보태그", ""+jsonResponse.get("메세지"))
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





}
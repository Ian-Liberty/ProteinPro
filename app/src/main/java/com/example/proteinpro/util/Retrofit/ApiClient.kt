package com.example.proteinpro.util.Retrofit

import com.example.proteinpro.util.Retrofit.ServerData.Companion.Server_URL
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



object ApiClient {


    private var retrofit: Retrofit? = null
    private val BASE_URL = Server_URL

    fun getApiClient(): Retrofit {

        val gson = GsonBuilder()
            .setDateFormat("E, dd MMMM yyyy HH:mm:ss X")
            .setLenient()
            .create()

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor)
            .build()

        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }

        return retrofit!!

    }

    fun getUserApiService(): UserDataInterface {
        return getApiClient().create(UserDataInterface::class.java)
    }

}
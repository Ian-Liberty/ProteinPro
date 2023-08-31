package com.example.proteinpro.util

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.example.proteinpro.util.Class.User
import com.google.gson.Gson

class PreferenceHelper(context: Context?) {

    private var userPrefs: SharedPreferences? = null
    private val gson = Gson()

    init {
        this.userPrefs = context?.getSharedPreferences("user_prefs", Activity.MODE_PRIVATE)
    }

    fun setIsLogin(jwt_Token: String?){
        val autoLoginEdit = userPrefs?.edit()
        autoLoginEdit?.putString("JWT_Token", jwt_Token)
        autoLoginEdit?.apply()
    }

    fun setCheckToken(jwt_Token: String?){
        val autoLoginEdit = userPrefs?.edit()
        autoLoginEdit?.putString("email_check_Token", jwt_Token)
        autoLoginEdit?.apply()
    }

    fun get_jwt_Token(): String? {
        return userPrefs?.getString("JWT_Token", "값이없음")
    }

    fun logout(){

        val autoLoginEdit = userPrefs?.edit()
        autoLoginEdit?.clear()
        autoLoginEdit?.apply()

    }

    fun saveUser(user: User) {

        val userJson = gson.toJson(user)
        val userEdit = userPrefs?.edit()
        userEdit?.putString("userJson", userJson)
        userEdit?.apply()

    }

    fun getUser(): User? {

        val userJson = userPrefs?.getString("userJson", null)
        return if (userJson != null) {
            gson.fromJson(userJson, User::class.java)
        } else {
            null
        }

    }



}
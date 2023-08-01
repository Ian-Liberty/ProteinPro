package com.example.proteinpro.util

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class PreferenceHelper(context: Context?) {

    private var user_prefs: SharedPreferences? = null

    init {
        this.user_prefs = context?.getSharedPreferences("user_prefs", Activity.MODE_PRIVATE)
    }

    fun setIsLogin(jwt_Token: String?){
        val autoLoginEdit = user_prefs?.edit()
        autoLoginEdit?.putString("JWT_Token", jwt_Token)
        autoLoginEdit?.apply()
    }

    fun get_jwt_Token(): String? {
        return user_prefs?.getString("JWT_Token", "값이없음")
    }

    fun logout(){

        val autoLoginEdit = user_prefs?.edit()
        autoLoginEdit?.clear()
        autoLoginEdit?.apply()

    }

}
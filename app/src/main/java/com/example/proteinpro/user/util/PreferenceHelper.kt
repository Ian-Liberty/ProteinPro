package com.example.proteinpro.user.util

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

    fun logout(){

        val autoLoginEdit = user_prefs?.edit()
        autoLoginEdit?.clear()
        autoLoginEdit?.apply()

    }

}
package com.sayed.thirdway.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sayed.thirdway.R
import com.sayed.thirdway.model.User
import javax.inject.Inject

class SPUtils//create constructor
    (context: Context) {

    //dec data
    private val sp: SharedPreferences
    companion object {
        //keys
        private val SP_USER_DATA = "SPUtils.UserData"
        private val SP_GOOGLE_TOKEN = "SPUtils.GoogleToken"

    }


    init {
        this.sp = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
    }

    //set and get user
    var user: User?
        get() {
            val data = sp.getString(SP_USER_DATA, null) ?: return null
            return Gson().fromJson<User>(data, object : TypeToken<User>() {

            }.type)
        }
        set(user) {
            val data = Gson().toJson(user)
            sp.edit().putString(SP_USER_DATA, data).apply()
        }

    //set and get google idToken
    var idToken: String?
        get() {
            return sp.getString(SP_GOOGLE_TOKEN,"")
        }
        set(value) {
            sp.edit().putString(SP_GOOGLE_TOKEN,value).apply()
        }

    //clear all data
    fun clear() {
        sp.edit().remove(SP_USER_DATA)
            .apply()
    }


}

package com.roman.application.util.storage

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.roman.application.base.BaseApplication
import com.roman.application.home.domain.model.response.city.Cities
import com.roman.application.home.domain.model.response.prayer.Prayers

object MySharePreference {

    private const val SAVE_DATA = "save_data"
    private const val CITY_NAME = "city_name"

    private val pref = BaseApplication.getInstance().getSharedPreferences("app", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = pref.edit()


    fun saveData(data: ArrayList<Prayers>) {
        editor.putString(SAVE_DATA, Gson().toJson(data))
        editor.commit()
    }

    fun getSavedData(): ArrayList<Prayers> {
       val data =  pref.getString(SAVE_DATA, "")
        if (data.isNullOrEmpty()){
            return ArrayList()
        }
        val gson = Gson()
        val response = object : TypeToken<ArrayList<Prayers>>() {}.type
        return gson.fromJson(data, response)
    }

    fun setCity(city: String){
        editor.putString(CITY_NAME, city)
        editor.commit()
    }

    fun getCity(): String?{
        return pref.getString(CITY_NAME, "")
    }
}
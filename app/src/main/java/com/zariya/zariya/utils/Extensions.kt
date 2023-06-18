package com.zariya.zariya.utils

import android.content.Context
import android.util.Log
import org.json.JSONArray

fun JSONArray.toCitiesList(): ArrayList<String> {
    val list = arrayListOf<String>()
    for (i in 0 until this.length()) {
        val jsonObject = this.getJSONObject(i)
        list.add(jsonObject.getString("name"))
    }

    return list
}

fun Context.getCities(): String? {
    var json: String? = null
    try {
        val inputStream = resources.assets.open("cities.json")
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()

        json = String(buffer)
    } catch (e: Exception) {
        Log.e("LocationFragment", e.printStackTrace().toString())
    }
    return json
}
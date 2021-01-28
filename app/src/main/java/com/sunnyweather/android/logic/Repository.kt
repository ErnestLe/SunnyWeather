package com.sunnyweather.android.logic

import androidx.lifecycle.liveData
import com.sunnyweather.android.logic.model.Place
import com.sunnyweather.android.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import java.lang.RuntimeException


object Repository {

    fun searchPlaces(query: String) = liveData(Dispatchers.IO) {
        val result = try {
            val placesResponse = SunnyWeatherNetwork.searchPlaces(query)
            if (placesResponse.status == "ok") {
                val places = placesResponse.places
                Result.success(places)
            } else {
                Result.failure(RuntimeException("response status is ${placesResponse.status}"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
        emit(result)
    }

}
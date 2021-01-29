package com.sunnyweather.android.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.sunnyweather.android.logic.Repository
import com.sunnyweather.android.logic.model.PlacesResponse.Place

class PlaceViewModel : ViewModel() {

    private val searchLiveData = MutableLiveData<String>()

    // 用于缓存 UI 的数据，以便屏幕旋转时不会丢失
    val placeList = ArrayList<Place>()

    // placesLiveData 会被 UI 层观察
    val placesLiveData = Transformations.switchMap(searchLiveData) {
        Repository.searchPlaces(it)
    }

    fun savePlace(place: Place) = Repository.savePlace(place)

    fun getSavedPlace() = Repository.getSavedPlace()

    fun isPlaceSaved() = Repository.isPlaceSaved()

    // UI 层提交了搜索请求，搜寻结果会异步到 placesLiveData
    fun searchPlaces(query: String) {
        searchLiveData.value = query
    }
}
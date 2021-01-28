package com.sunnyweather.android.logic.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object SunnyWeatherNetwork {

    private val placeService = ServiceCreator.create<PlaceService>()

    suspend fun searchPlaces(query: String) = placeService.searchPlaces(query).await()

    // suspend 关键字表示该函数"可能"会挂起当前协程
    // suspend 修饰的函数必须在协程(协程作用域)中运行
    private suspend fun <T> Call<T>.await() : T {
        // suspendCoroutine 表示创建一个新的协程并挂起调用挂起函数的协程
        // continuation 实例用于何时如何恢复被挂起的协程
        return suspendCoroutine {
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if(body != null) {
                        it.resume(body)
                    }
                    else {
                        it.resumeWithException(RuntimeException("Response body is null"))
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    it.resumeWithException(t)
                }
            })
        }
    }

}
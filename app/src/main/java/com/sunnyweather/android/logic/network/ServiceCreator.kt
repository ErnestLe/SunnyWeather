package com.sunnyweather.android.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceCreator {

    private const val BASE_URL = "https://api.caiyunapp.com/"

    // 利用网络通讯参数构建 Retrofit 的实例
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // 构建完 retrofit 后，利用 retrofit 这个实例创建 serviceClass 接口的实例
    fun <T> create(serviceClass: Class<T>) : T = retrofit.create(serviceClass)

    // 对上一个方法的泛型封装
    inline fun <reified T> create(): T = create(T::class.java)
}
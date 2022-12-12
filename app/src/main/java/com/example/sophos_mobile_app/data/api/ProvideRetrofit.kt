package com.example.sophos_mobile_app.data.api

import com.example.sophos_mobile_app.utils.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object ProvideRetrofit {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val api: ApiService = retrofit.create(ApiService::class.java)
}
package com.monicatifanyz.digimagz.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {

    private var retrofit: Retrofit? = null
    private var httpLoggingInterceptor: HttpLoggingInterceptor? = null
    private var okHttpClient: OkHttpClient? = null

    fun getRetrofit(): Retrofit? {
        if (retrofit == null) {
            httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor!!.setLevel(HttpLoggingInterceptor.Level.BODY)
            okHttpClient = OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor!!)
                .build()
            retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient) //.baseUrl("http://pn10mobprd.ptpn10.co.id:8080/digimagz/api/")
                .baseUrl("http://digimon.kristomoyo.com/api/")
                .build()
        }
        return retrofit
    }

}
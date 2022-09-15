package com.marsuch.weatherappforeternal.network

import com.marsuch.weatherappforeternal.network.dataclasses.WeatherInfo
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "http://api.openweathermap.org/data/2.5/"

private val moshi = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())
    .build()

class ApiClient {
    fun getClient(): Retrofit {
        val interceptor = HttpLoggingInterceptor();
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor). build()

        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BASE_URL)
            .client(client)
            .build()

        return retrofit
    }
}

interface WeatherApiService {
    @GET("forecast/daily")
    fun getWeatherData(
        @Query("lat") lat: String, //Zeměpisná délka
        @Query("lon") lon: String, //Zeměpisná šířka
        @Query("appid") apiKey: String, //Api klíč
        @Query("units") units: String, //Jednotky ve kterých mají být zobrazovány údaje ""metrické/imperiální
        @Query("cnt") cnt: String //Početn dní na kterou má být zobrazena předpověď
    ): Call<WeatherInfo>
}

object WeatherApi {
    val retrofitService : WeatherApiService by lazy {
        ApiClient().getClient().create(WeatherApiService::class.java)
    }
}
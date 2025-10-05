package com.keagan.complete.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Minimal Retrofit factory so you can plug it in later with one line.
 * Nothing calls this yet.
 */
object NetworkModule {

    private const val BASE_URL = "https://api.yourdomain.com/" // TODO: replace

    private val client by lazy {
        val log = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
        OkHttpClient.Builder()
            .addInterceptor(log)
            .build()
    }

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val motivationService: MotivationService by lazy {
        retrofit.create(MotivationService::class.java)
    }

    val streakService: StreakService by lazy {
        retrofit.create(StreakService::class.java)
    }
}

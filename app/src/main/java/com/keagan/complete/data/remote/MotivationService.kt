package com.keagan.complete.data.remote

import retrofit2.Call
import retrofit2.http.GET

/**
 * PLACEHOLDER. Wire this later in a Repo/ViewModel.
 */
interface MotivationService {
    // e.g. GET https://api.yourdomain.com/motivation/daily
    @GET("motivation/daily")
    fun getDailyQuote(): Call<QuoteDto>
}

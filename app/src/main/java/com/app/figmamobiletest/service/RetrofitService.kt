package com.app.figmamobiletest.service

import com.app.figmamobiletest.model.profile.DashboardProfileResponse
import com.app.figmamobiletest.model.rides.DashboardRidesResponse
import com.app.figmamobiletest.utils.apiUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface RetrofitService {

    @GET("rides")
    @Headers("accept:application/json")
    suspend fun getRidesList(): Response<DashboardRidesResponse>

    @GET("user")
    @Headers("accept:application/json")
    suspend fun getUserProfile(): Response<DashboardProfileResponse>

    companion object {

        val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        var httpClient = OkHttpClient.Builder().addInterceptor(logging)

        var retrofitService: RetrofitService? = null
        fun getInstance(): RetrofitService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(apiUrl.API_URL)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(RetrofitService::class.java)
            }
            return retrofitService!!
        }

    }
}
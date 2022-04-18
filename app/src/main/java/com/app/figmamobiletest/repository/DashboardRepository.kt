package com.app.figmamobiletest.repository

import com.app.figmamobiletest.model.profile.DashboardProfileResponse
import com.app.figmamobiletest.model.rides.DashboardRidesResponse
import com.app.figmamobiletest.service.RetrofitService
import retrofit2.Response

class DashboardRepository constructor(
    private val retrofitService: RetrofitService
) {

    suspend fun getRidesList(): Response<DashboardRidesResponse> {
        return retrofitService.getRidesList()
    }

    suspend fun getUserProfile(): Response<DashboardProfileResponse> {
        return retrofitService.getUserProfile()
    }
}
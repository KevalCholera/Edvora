package com.app.figmamobiletest.model.city

import android.os.Parcelable
import com.app.figmamobiletest.model.rides.Data
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


class DashboardCityResponse : ArrayList<DashboardCityResponseData>()

@Parcelize
data class DashboardCityResponseData(
    @SerializedName("city_name")
    val cityName: String?
) : Parcelable
package com.app.figmamobiletest.model.profile

import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class DashboardProfileResponse(
    @SerializedName("name")
    val name: String?,
    @SerializedName("station_code")
    val stationCode: Int?,
    @SerializedName("url")
    val url: String?
) : Parcelable
package com.app.figmamobiletest.model.state

import android.os.Parcelable
import com.app.figmamobiletest.model.rides.Data
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

class DashboardStateResponse : ArrayList<DashboardStateResponseData>()

@Parcelize
data class DashboardStateResponseData(
    @SerializedName("state_name")
    val stateName: String?
) : Parcelable
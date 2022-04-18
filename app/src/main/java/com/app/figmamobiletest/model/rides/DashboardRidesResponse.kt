package com.app.figmamobiletest.model.rides

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class DashboardRidesResponse : ArrayList<Data>(), Parcelable

@Parcelize
data class Data(
    @SerializedName("city")
    val city: String?,
    @SerializedName("ride_id")
    var rideId: String?,
    @SerializedName("date")
    val date: String?,
    @SerializedName("datePast")
    var datePast: Boolean?,
    @SerializedName("dateUpcoming")
    var dateUpcoming: Boolean?,
    @SerializedName("dateNearest")
    var dateNearest: Boolean?,
    @SerializedName("destination_station_code")
    val destinationStationCode: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("map_url")
    val mapUrl: String?,
    @SerializedName("origin_station_code")
    val originStationCode: Int?,
    @SerializedName("state")
    val state: String?,
    @SerializedName("station_path")
    val stationPath: List<Int>?
) : Parcelable
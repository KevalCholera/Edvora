package com.app.figmamobiletest.design.activity

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.figmamobiletest.base.Status
import com.app.figmamobiletest.model.city.DashboardCityResponseData
import com.app.figmamobiletest.model.profile.DashboardProfileViewState
import com.app.figmamobiletest.model.rides.DashboardRidesResponse
import com.app.figmamobiletest.model.rides.DashboardRidesViewState
import com.app.figmamobiletest.model.state.DashboardStateResponseData
import com.app.figmamobiletest.repository.DashboardRepository
import com.app.figmamobiletest.utils.STATUS_CODE_FAILURE
import com.app.figmamobiletest.utils.STATUS_CODE_INTERNAL_ERROR
import com.app.figmamobiletest.utils.STATUS_CODE_SUCCESS
import com.app.figmamobiletest.utils.STATUS_CODE_TOKEN_EXPIRE
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class DashboardActivityViewModel constructor(private val dashboardRepository: DashboardRepository) :
    ViewModel() {

    private var _dashboardRideResponse: MutableLiveData<DashboardRidesViewState> = MutableLiveData()
    private var _dashboardProfileResponse: MutableLiveData<DashboardProfileViewState> =
        MutableLiveData()
    private var currentStationCode = -1
    private var currentStationCodeArray = arrayListOf<Int>()
    var _dashboardStateResponse: ObservableArrayList<DashboardStateResponseData> =
        ObservableArrayList<DashboardStateResponseData>()
    var _dashboardCityResponse: ObservableArrayList<DashboardCityResponseData> =
        ObservableArrayList<DashboardCityResponseData>()

    val dashboardRideResponse: LiveData<DashboardRidesViewState>
        get() {
            return _dashboardRideResponse
        }

    val dashboardProfileResponse: LiveData<DashboardProfileViewState>
        get() {
            return _dashboardProfileResponse
        }

    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _dashboardRideResponse.postValue(
            DashboardRidesViewState(
                Status.ERROR,
                "Something went wrong"
            )
        )
        _dashboardRideResponse.postValue(
            DashboardRidesViewState(
                Status.ERROR,
                "Something went wrong"
            )
        )
    }


    fun getRidesList() {
        _dashboardRideResponse.postValue(
            DashboardRidesViewState(
                Status.LOADING,
                null,
                null
            )
        )
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = dashboardRepository.getRidesList()
            withContext(Dispatchers.Main) {
                when (response.code()) {
                    STATUS_CODE_SUCCESS -> {
                        _dashboardRideResponse.postValue(
                            DashboardRidesViewState(
                                Status.SUCCESS,
                                null,
                                getRideFiltered(response.body())
                            )
                        )
                    }
                    STATUS_CODE_FAILURE -> {
                        _dashboardRideResponse.postValue(
                            DashboardRidesViewState(
                                Status.FAIL,
                                "Something went wrong",
                                null
                            )
                        )
                    }
                    STATUS_CODE_INTERNAL_ERROR -> {
                        _dashboardRideResponse.postValue(
                            DashboardRidesViewState(
                                Status.ERROR,
                                "Internal Error",
                                null
                            )
                        )
                    }
                    STATUS_CODE_TOKEN_EXPIRE -> _dashboardRideResponse.postValue(
                        DashboardRidesViewState(
                            Status.UNAUTHORISED,
                            response.message(),
                            null
                        )
                    )
                    else -> _dashboardRideResponse.postValue(
                        DashboardRidesViewState(
                            Status.ERROR,
                            "Something went wrong"
                        )
                    )
                }
            }
        }
    }

    fun getProfile() {
        _dashboardProfileResponse.postValue(
            DashboardProfileViewState(
                Status.LOADING,
                null,
                null
            )
        )
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = dashboardRepository.getUserProfile()
            withContext(Dispatchers.Main) {
                when (response.code()) {
                    STATUS_CODE_SUCCESS -> {
                        val responseData = response.body()
                        if (responseData?.stationCode != null)
                            currentStationCode = responseData.stationCode

                        currentStationCodeArray = getCurrentCodeArray(currentStationCode)

                        _dashboardProfileResponse.postValue(
                            DashboardProfileViewState(
                                Status.SUCCESS,
                                null,
                                responseData
                            )
                        )
                    }
                    STATUS_CODE_FAILURE -> {
                        _dashboardProfileResponse.postValue(
                            DashboardProfileViewState(
                                Status.FAIL,
                                "Something went wrong",
                                null
                            )
                        )
                    }
                    STATUS_CODE_INTERNAL_ERROR -> {
                        _dashboardProfileResponse.postValue(
                            DashboardProfileViewState(
                                Status.ERROR,
                                "Internal Error",
                                null
                            )
                        )
                    }
                    STATUS_CODE_TOKEN_EXPIRE ->
                        _dashboardProfileResponse.postValue(
                            DashboardProfileViewState(
                                Status.UNAUTHORISED,
                                response.message(),
                                null
                            )
                        )
                    else -> _dashboardProfileResponse.postValue(
                        DashboardProfileViewState(
                            Status.ERROR,
                            "Something went wrong"
                        )
                    )
                }
            }
        }
    }

    private fun getRideFiltered(response: DashboardRidesResponse?): DashboardRidesResponse? {
        val sdf = SimpleDateFormat("MM/dd/yyyy hh:mm a")

        if (response != null) {

            for ((i, data) in response.withIndex()) {

                if (_dashboardStateResponse.isNotEmpty())
                    for (ki in 0 until _dashboardStateResponse.size) {
                        if (_dashboardStateResponse[ki].stateName == data.state.toString())
                            break
                        else if (ki == _dashboardStateResponse.size - 1)
                            _dashboardStateResponse.add(DashboardStateResponseData(data.state.toString()))
                    }
                else
                    _dashboardStateResponse.add(DashboardStateResponseData(data.state.toString()))

                if (_dashboardCityResponse.isNotEmpty())
                    for (ji in 0 until _dashboardCityResponse.size) {
                        if (_dashboardCityResponse[ji].cityName == data.state.toString())
                            break
                        else if (ji == _dashboardCityResponse.size - 1)
                            _dashboardCityResponse.add(DashboardCityResponseData(data.city.toString()))
                    }
                else
                    _dashboardCityResponse.add(DashboardCityResponseData(data.city.toString()))

                _dashboardCityResponse.sortBy { it.cityName }
                _dashboardStateResponse.sortBy { it.stateName }

                when {
                    i < 10 -> data.rideId = "00" + (i).toString()
                    i in 11..99 -> data.rideId = "0" + (i).toString()
                    else -> data.rideId = (i).toString()
                }

                val strDate: Date? = sdf.parse(data.date.toString())
                Log.i("TAG", "getRideFiltered: $strDate")
                val currentDate: Date = Calendar.getInstance().time

                if (strDate != null) {
                    data.dateUpcoming = currentDate.time < strDate.time
                    data.datePast = currentDate.time > strDate.time

                    if (data.stationPath != null && data.stationPath.isNotEmpty()) {

                        for (j in 0 until currentStationCodeArray.size) {
                            val currentStationCodeArrayData = currentStationCodeArray[j]

                            for (stationPathData in data.stationPath) {
                                if (currentStationCodeArrayData == stationPathData) {
                                    data.dateNearest = true
                                    break
                                } else {
                                    if (j == currentStationCodeArray.size - 1)
                                        data.dateNearest = false
                                }
                            }
                        }
                    }
                }
            }
        }
        return response
    }

    private fun getCurrentCodeArray(currentCode: Int): ArrayList<Int> {
        var currentCodeIncrement = currentCode
        var currentCodeDecrement = currentCode
        val arrayList = arrayListOf<Int>()
        arrayList.add(currentCode)

        for (i in 1..10) {
            arrayList.add(currentCodeIncrement++)
            arrayList.add(currentCodeDecrement--)
        }

        return arrayList
    }
}
package com.app.figmamobiletest.design.fragments.past

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.figmamobiletest.databinding.FragmentPastBinding
import com.app.figmamobiletest.design.fragments.nearest.NearestFragmentAdapter
import com.app.figmamobiletest.model.rides.DashboardRidesResponse
import com.app.figmamobiletest.model.rides.Data
import com.app.figmamobiletest.utils.PrefUtilsConstants
import com.embie.droid.fragments.common.BaseFragment
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject

class PastFragment : BaseFragment() {

    private lateinit var binding: FragmentPastBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPastBinding.inflate(inflater, container, false)

        val rideListResponse: DashboardRidesResponse? = requireArguments().getParcelable(
            PrefUtilsConstants.RIDE_LIST
        )
        var filterData = JSONObject()
        var isFilter = false
        var stateName = ""
        var cityName = ""

        try {
            filterData =
                JSONObject(objSharedPref.getString(PrefUtilsConstants.FILTER_DATA).toString())
            isFilter = filterData.getBoolean(PrefUtilsConstants.IS_FILTER)
            stateName = filterData.getString(PrefUtilsConstants.STATE_NAME)
            cityName = filterData.getString(PrefUtilsConstants.CITY_NAME)
        } catch (e: JSONException) {
            print(e.localizedMessage)
        }

        Log.i(TAG, "onCreateView: $filterData")

        val rideListResponseTemp: ArrayList<Data> = arrayListOf()
        val currentCode = objSharedPref.getString(PrefUtilsConstants.STATION_CODE)

        if (rideListResponse != null) {
            for (data in rideListResponse) {
                if (data.datePast!!) {
                    if (isFilter) {
                        if (stateName.uppercase() == data.state.toString()
                                .uppercase() || cityName.uppercase() == data.city.toString()
                                .uppercase()
                        ) {
                            rideListResponseTemp.add(data)
                        }
                    } else {
                        rideListResponseTemp.add(data)
                    }
                }
            }
        }

        Log.i(TAG, "rideListResponseTemp: ${Gson().toJson(rideListResponseTemp)}")

        binding.rvPast.layoutManager = LinearLayoutManager(mContext)
        binding.rvPast.adapter =
            NearestFragmentAdapter(rideListResponseTemp, currentCode.toString())


        return binding.root
    }
}
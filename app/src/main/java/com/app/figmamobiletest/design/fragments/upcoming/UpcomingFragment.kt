package com.app.figmamobiletest.design.fragments.upcoming

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.figmamobiletest.databinding.FragmentUpcomingBinding
import com.app.figmamobiletest.design.fragments.nearest.NearestFragmentAdapter
import com.app.figmamobiletest.model.rides.DashboardRidesResponse
import com.app.figmamobiletest.model.rides.Data
import com.app.figmamobiletest.utils.PrefUtilsConstants
import com.embie.droid.fragments.common.BaseFragment
import org.json.JSONException
import org.json.JSONObject

class UpcomingFragment : BaseFragment() {

    private lateinit var binding: FragmentUpcomingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpcomingBinding.inflate(inflater, container, false)

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
                if (data.dateUpcoming!!) {
                    if (isFilter) {
                        if (stateName.toString().uppercase() == data.state.toString()
                                .uppercase() || cityName.toString()
                                .uppercase() == data.city.toString().uppercase()
                        ) {
                            rideListResponseTemp.add(data)
                        }
                    } else {
                        rideListResponseTemp.add(data)
                    }
                }
            }
        }

        binding.rvUpcoming.layoutManager = LinearLayoutManager(mContext)
        binding.rvUpcoming.adapter =
            NearestFragmentAdapter(rideListResponseTemp, currentCode.toString())

        return binding.root
    }
}
package com.app.figmamobiletest.design.activity

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.app.figmamobiletest.R
import com.app.figmamobiletest.base.BaseActivity
import com.app.figmamobiletest.base.Status
import com.app.figmamobiletest.databinding.ActivityDashboardBinding
import com.app.figmamobiletest.design.fragments.nearest.NearestFragment
import com.app.figmamobiletest.design.fragments.past.PastFragment
import com.app.figmamobiletest.design.fragments.upcoming.UpcomingFragment
import com.app.figmamobiletest.model.rides.DashboardRidesResponse
import com.app.figmamobiletest.repository.DashboardRepository
import com.app.figmamobiletest.service.RetrofitService
import com.app.figmamobiletest.utils.PrefUtilsConstants.CITY_NAME
import com.app.figmamobiletest.utils.PrefUtilsConstants.FILTER_DATA
import com.app.figmamobiletest.utils.PrefUtilsConstants.IS_FILTER
import com.app.figmamobiletest.utils.PrefUtilsConstants.RIDE_LIST
import com.app.figmamobiletest.utils.PrefUtilsConstants.STATE_NAME
import com.app.figmamobiletest.utils.PrefUtilsConstants.STATION_CODE
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import org.json.JSONObject

class DashboardActivity : BaseActivity() {
    val binding: ActivityDashboardBinding by binding(R.layout.activity_dashboard)

    private lateinit var fragment: Fragment
    private lateinit var fragmentManager: FragmentManager
    private lateinit var fragmentTransaction: FragmentTransaction

    private lateinit var viewModel: DashboardActivityViewModel
    lateinit var rideListResponse: DashboardRidesResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
        initViews(retrofitService)
        onClickListener()
    }

    private fun onClickListener() {
        binding.tvDashboardFilter.setOnClickListener {
            binding.llDashboardFilter.visibility = View.VISIBLE
        }

        binding.viewDashboardFilter.setOnClickListener {
            binding.llDashboardFilter.visibility = View.GONE
        }

        binding.tvDashboardFilterState.setOnClickListener {
            openOccasionsPopupOptionsForState(true)
        }

        binding.tvDashboardFilterCity.setOnClickListener {
            openOccasionsPopupOptionsForState(false)

        }

        binding.btDashboardClear.setOnClickListener {
            binding.llDashboardFilter.visibility = View.GONE
            binding.tvDashboardFilterState.text = ""
            binding.tvDashboardFilterCity.text = ""
            val jsonData = JSONObject()
            jsonData.put(CITY_NAME, binding.tvDashboardFilterCity.text.toString())
            jsonData.put(STATE_NAME, binding.tvDashboardFilterState.text.toString())
            jsonData.put(IS_FILTER, false)
            objSharedPref.putString(FILTER_DATA, jsonData.toString())
            initializeTabLayout()
            binding.tlDashboardTabLayout.getTabAt(0)!!.select()

        }

        binding.btDashboardSubmit.setOnClickListener {
            binding.llDashboardFilter.visibility = View.GONE
            val jsonData = JSONObject()
            jsonData.put(CITY_NAME, binding.tvDashboardFilterCity.text.toString())
            jsonData.put(STATE_NAME, binding.tvDashboardFilterState.text.toString())
            jsonData.put(IS_FILTER, true)
            objSharedPref.putString(FILTER_DATA, jsonData.toString())
            Log.i(TAG, "btDashboardSubmit: ${objSharedPref.getString(FILTER_DATA)}")
            initializeTabLayout()
            binding.tlDashboardTabLayout.getTabAt(0)!!.select()
        }
    }

    private fun initializeData() {

        fragment = NearestFragment().apply {
            arguments = Bundle().apply {
                putParcelable(RIDE_LIST, rideListResponse)
                putString(STATE_NAME, "")
                putString(CITY_NAME, "")
                putBoolean(IS_FILTER, false)
            }
        }
        fragmentManager = supportFragmentManager
        fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.flDashboard, fragment)
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        fragmentTransaction.commit()

    }

    private fun initViews(retrofitService: RetrofitService) {
        viewModel = ViewModelProvider(
            this,
            DashboardActivityModelFactory(
                DashboardRepository(
                    retrofitService
                )
            )
        ).get(DashboardActivityViewModel::class.java)

        viewModel.dashboardRideResponse.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.progressCircular.visibility = View.GONE
                    if (it.response != null) {
                        rideListResponse = it.response
                        logI("Changed Response", Gson().toJson(it))
                        initializeTabLayout()

                    }
                }
                Status.ERROR -> {
                    binding.progressCircular.visibility = View.GONE
                }
                Status.LOADING -> {
                    binding.progressCircular.visibility = View.VISIBLE
                }
                Status.FAIL -> {
                    binding.progressCircular.visibility = View.GONE
                }
                else -> {
                    binding.progressCircular.visibility = View.GONE
                }
            }
        })
        viewModel.dashboardProfileResponse.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.progressCircular.visibility = View.GONE

                    if (it.response != null) {
                        binding.tvDashboardTitle.text = it.response.name.toString()

                        Glide.with(this)
                            .load(it.response.url.toString())
                            .placeholder(R.mipmap.ic_launcher)
                            .into(binding.civDashboardProfilePic)

                        objSharedPref.putString(STATION_CODE, it.response.stationCode.toString())
                        viewModel.getRidesList()
                    }
                }
                Status.ERROR -> {
                    binding.progressCircular.visibility = View.GONE
                }
                Status.LOADING -> {
                    binding.progressCircular.visibility = View.VISIBLE
                }
                Status.FAIL -> {
                    binding.progressCircular.visibility = View.GONE
                }
                else -> {
                    binding.progressCircular.visibility = View.GONE
                }
            }
        })

        viewModel.getProfile()
    }

    private fun initializeTabLayout() {
        initializeData()

        binding.tlDashboardTabLayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {

                when (tab.position) {
                    0 -> fragment = NearestFragment().apply {
                        arguments = Bundle().apply {
                            putParcelable(RIDE_LIST, rideListResponse)
                        }
                    }
                    1 -> fragment = UpcomingFragment().apply {
                        arguments = Bundle().apply {
                            putParcelable(RIDE_LIST, rideListResponse)
                        }
                    }
                    2 -> fragment = PastFragment().apply {
                        arguments = Bundle().apply {
                            putParcelable(RIDE_LIST, rideListResponse)
                        }
                    }
                }
                val fm = supportFragmentManager
                val ft = fm.beginTransaction()
                ft.replace(R.id.flDashboard, fragment)
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                ft.commit()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    private fun openOccasionsPopupOptionsForState(isState: Boolean) {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose!")

        if (isState) {
            val stateName = viewModel._dashboardStateResponse
            val stateNameArray = arrayOfNulls<String>(stateName.size)
            for (i in stateNameArray.indices)
                stateNameArray[i] = stateName[i].stateName.toString()

            builder.setItems(stateNameArray) { dialog, which ->
                binding.tvDashboardFilterState.text = stateNameArray[which].toString()
                dialog.dismiss()
            }
        } else {
            val cityName = viewModel._dashboardCityResponse
            val cityNameArray = arrayOfNulls<String>(cityName.size)
            for (i in cityNameArray.indices)
                cityNameArray[i] = cityName[i].cityName.toString()

            builder.setItems(cityNameArray) { dialog, which ->
                binding.tvDashboardFilterCity.text = cityNameArray[which].toString()
                dialog.dismiss()
            }
        }
        val dialog = builder.create()
        dialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        objSharedPref.logout()
    }
}
package com.app.figmamobiletest.design.fragments.nearest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.figmamobiletest.databinding.ItemDataBinding
import com.app.figmamobiletest.model.rides.Data
import com.bumptech.glide.Glide

class NearestFragmentAdapter(
    private val rideListResponseTemp: ArrayList<Data>,
    private val currentCode: String
) :
    RecyclerView.Adapter<NearestFragmentAdapter.ViewHolder>() {
    lateinit var binding: ItemDataBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = rideListResponseTemp[position]

        Glide.with(binding.root.rootView)
            .load(data.mapUrl)
            .into(binding.ivItemDataProfile)

        binding.tvItemDataCityName.text = data.city.toString()
        binding.tvItemDataStateName.text = data.state.toString()
        binding.tvItemDataRideNoValue.text = data.id.toString()
        binding.tvItemDataOriginalStationNoValue.text = data.originStationCode.toString()
        binding.tvItemDataStationPathValue.text = data.stationPath.toString()
        binding.tvItemDataDateValue.text = data.date.toString()
        val distance = data.destinationStationCode.toString().toInt() - currentCode.toInt()
        binding.tvItemDataDistanceValue.text = distance.toString()

    }

    override fun getItemCount(): Int {
        return rideListResponseTemp.size
    }

    class ViewHolder(
        private val binding: ItemDataBinding
    ) : RecyclerView.ViewHolder(binding.root)
}

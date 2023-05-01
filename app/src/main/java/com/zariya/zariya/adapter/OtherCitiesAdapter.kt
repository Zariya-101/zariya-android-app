package com.zariya.zariya.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zariya.zariya.databinding.OtherCityItemBinding


class OtherCitiesAdapter (
    private val cityNames: Array<String>,

) : RecyclerView.Adapter<OtherCitiesAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val mBinding = OtherCityItemBinding.inflate(
            LayoutInflater.from(
                parent.context
            ), parent, false
        )
        return MyViewHolder(mBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.mBinding.cityNameTextView.setText(cityNames.get(position))
    }

    override fun getItemCount(): Int {
        return cityNames.size
    }

    inner class MyViewHolder(var mBinding: OtherCityItemBinding) :
        RecyclerView.ViewHolder(
            mBinding.root
        )
}
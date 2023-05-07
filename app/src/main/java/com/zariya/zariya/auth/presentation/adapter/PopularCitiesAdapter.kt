package com.zariya.zariya.auth.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zariya.zariya.auth.presentation.adapter.PopularCitiesAdapter.MyViewHolder
import com.zariya.zariya.databinding.PopularCityItemBinding

class PopularCitiesAdapter(
    private val cityNames: Array<String>,
    private val cityImages: IntArray

) : RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val mBinding = PopularCityItemBinding.inflate(
            LayoutInflater.from(
                parent.context
            ), parent, false
        )
        return MyViewHolder(mBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.mBinding.cityImageView.setImageResource(cityImages.get(position))
        holder.mBinding.cityNameTextView.setText(cityNames.get(position))
    }

    override fun getItemCount(): Int {
        return cityImages.size
    }

    inner class MyViewHolder(var mBinding: PopularCityItemBinding) :
        RecyclerView.ViewHolder(
            mBinding.root
        )
}
package com.zariya.zariya.auth.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.zariya.zariya.R
import com.zariya.zariya.databinding.ItemPopularCityBinding

class PopularCitiesAdapter(
    private val cityNames: Array<String>,
    private val cityImages: IntArray,
    private val onItemClick: (String?) -> Unit
) : RecyclerView.Adapter<PopularCitiesAdapter.PopularCityViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PopularCityViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_popular_city,
            parent, false
        )
    )

    override fun onBindViewHolder(holder: PopularCityViewHolder, position: Int) {
        holder.mBinding.cityImageView.setImageResource(cityImages[position])
        holder.mBinding.cityNameTextView.text = cityNames[position]
        holder.mBinding.root.setOnClickListener {
            onItemClick(cityNames[position])
        }
    }

    override fun getItemCount() = cityImages.size

    inner class PopularCityViewHolder(val mBinding: ItemPopularCityBinding) :
        RecyclerView.ViewHolder(mBinding.root)
}
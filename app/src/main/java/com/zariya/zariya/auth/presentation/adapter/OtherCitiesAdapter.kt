package com.zariya.zariya.auth.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.zariya.zariya.R
import com.zariya.zariya.databinding.ItemOtherCityBinding

class OtherCitiesAdapter(
    private val cities: ArrayList<String>,
    private val onItemClick: (String?) -> Unit
) : RecyclerView.Adapter<OtherCitiesAdapter.OtherCitiesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = OtherCitiesViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_other_city,
            parent, false
        )
    )

    override fun onBindViewHolder(holder: OtherCitiesViewHolder, position: Int) {
        holder.binding.tvCity.text = cities[position]
        holder.binding.root.setOnClickListener {
            onItemClick(cities[position])
        }
    }

    override fun getItemCount() = cities.size

    inner class OtherCitiesViewHolder(var binding: ItemOtherCityBinding) :
        RecyclerView.ViewHolder(binding.root)
}
package com.zariya.zariya.workshop.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.zariya.zariya.R
import com.zariya.zariya.databinding.ItemFilterWorkshopBinding
import com.zariya.zariya.workshop.data.model.FilterWorkshops

class FilterWorkshopsAdapter(private val list: List<FilterWorkshops>) :
    RecyclerView.Adapter<FilterWorkshopsAdapter.FilterWorkshopsViewHolder>() {

    inner class FilterWorkshopsViewHolder(val binding: ItemFilterWorkshopBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = FilterWorkshopsViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_filter_workshop,
            parent, false
        )
    )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: FilterWorkshopsViewHolder, position: Int) {
        holder.binding.filter = list[position]
    }
}
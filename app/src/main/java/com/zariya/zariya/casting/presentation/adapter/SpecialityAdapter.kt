package com.zariya.zariya.casting.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.zariya.zariya.R
import com.zariya.zariya.databinding.ItemSpecialityBinding

class SpecialityAdapter(private val list: List<String>) :
    RecyclerView.Adapter<SpecialityAdapter.SpecialityViewHolder>() {

    inner class SpecialityViewHolder(val binding: ItemSpecialityBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SpecialityViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_speciality,
            parent, false
        )
    )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: SpecialityViewHolder, position: Int) {
        holder.binding.speciality = list[position]
    }
}
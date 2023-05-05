package com.zariya.zariya.workshop.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.zariya.zariya.R
import com.zariya.zariya.databinding.ItemWorkshopBinding
import com.zariya.zariya.workshop.data.model.Workshop

class WorkshopsAdapter(
    private val list: List<Workshop>,
    private val onCardClicked: (Workshop) -> Unit
) :
    RecyclerView.Adapter<WorkshopsAdapter.WorkshopsViewHolder>() {

    inner class WorkshopsViewHolder(val binding: ItemWorkshopBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = WorkshopsViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_workshop,
            parent, false
        )
    )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: WorkshopsViewHolder, position: Int) {
        holder.binding.workshop = list[position]
        holder.binding.cv.setOnClickListener {
            onCardClicked(list[position])
        }
    }
}
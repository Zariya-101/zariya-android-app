package com.zariya.zariya.casting.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.zariya.zariya.R
import com.zariya.zariya.common.data.model.SelectableListElement
import com.zariya.zariya.databinding.ItemComplexionSelectorBinding

class ComplexionAdapter(private val list: List<SelectableListElement>, private val onCardClicked: (String) -> Unit) :
    RecyclerView.Adapter<ComplexionAdapter.ComplexionViewHolder>() {

    inner class ComplexionViewHolder(val binding: ItemComplexionSelectorBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ComplexionViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_complexion_selector,
            parent, false
        )
    )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ComplexionViewHolder, position: Int) {
        holder.binding.element = list[position]
        holder.binding.root.setOnClickListener {
            list.forEach { it.isSelected = false }
            list[position].isSelected = true

            onCardClicked(list[position].text)

            notifyDataSetChanged()
        }
    }
}
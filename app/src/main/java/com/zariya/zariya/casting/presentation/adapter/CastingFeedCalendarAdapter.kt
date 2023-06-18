package com.zariya.zariya.casting.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.zariya.zariya.R
import com.zariya.zariya.databinding.ItemCastingFeedCalendarBinding

class CastingFeedCalendarAdapter(private val onItemClick: () -> Unit) :
    RecyclerView.Adapter<CastingFeedCalendarAdapter.CastingFeedCalendarViewHolder>() {

    inner class CastingFeedCalendarViewHolder(val binding: ItemCastingFeedCalendarBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = CastingFeedCalendarViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_casting_feed_calendar,
            parent, false
        )
    )

    override fun getItemCount() = 4

    override fun onBindViewHolder(holder: CastingFeedCalendarViewHolder, position: Int) {
        holder.binding.root.setOnClickListener { onItemClick() }
    }


}
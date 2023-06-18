package com.zariya.zariya.workshop.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.zariya.zariya.R
import com.zariya.zariya.databinding.ItemRecentReviewBinding
import com.zariya.zariya.workshop.data.model.WorkshopReview

class RecentReviewsAdapter(private val reviews: List<WorkshopReview>) :
    RecyclerView.Adapter<RecentReviewsAdapter.RecentReviewsViewHolder>() {

    inner class RecentReviewsViewHolder(val binding: ItemRecentReviewBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RecentReviewsViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_recent_review,
            parent, false
        )
    )

    override fun getItemCount() = reviews.size

    override fun onBindViewHolder(holder: RecentReviewsViewHolder, position: Int) {
        holder.binding.review = reviews[position]
    }
}
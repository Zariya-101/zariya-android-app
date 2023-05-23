package com.zariya.zariya.casting.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.zariya.zariya.R
import com.zariya.zariya.databinding.ItemUploadImageBinding

class AddImageAdapter(
    private val list: List<String?>,
    private val onItemClick: (String?) -> Unit,
    private val onCloseClick: (String?) -> Unit
) : RecyclerView.Adapter<AddImageAdapter.AddImageViewHolder>() {

    inner class AddImageViewHolder(val binding: ItemUploadImageBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = AddImageViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_upload_image,
            parent, false
        )
    )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: AddImageViewHolder, position: Int) {
        holder.binding.image = list[position]
        holder.binding.root.setOnClickListener { onItemClick(list[position]) }
        holder.binding.ivClose.setOnClickListener { onCloseClick(list[position]) }
        if (list[position].isNullOrEmpty().not()) {
            holder.binding.ivImage.setImageURI(list[position]?.toUri())
        }
    }
}
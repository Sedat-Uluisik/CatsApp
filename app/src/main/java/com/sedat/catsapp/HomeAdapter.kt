package com.sedat.catsapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.sedat.catsapp.databinding.CatItemLayoutBinding
import com.sedat.catsapp.model.CatItem
import javax.inject.Inject


class HomeAdapter @Inject constructor(
    private val glide: RequestManager
): PagingDataAdapter<CatItem, HomeAdapter.Holder>(diffUtil()) {

    class diffUtil : DiffUtil.ItemCallback<CatItem>(){
        override fun areItemsTheSame(oldItem: CatItem, newItem: CatItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CatItem, newItem: CatItem): Boolean {
            return oldItem == newItem
        }

    }

    private var onItemClickListener: (() -> Unit) ?= null
    fun catItemClick(listener: ()-> Unit){
        onItemClickListener = listener
    }

    override fun onBindViewHolder(holder: HomeAdapter.Holder, position: Int) {
        val cat = getItem(position)

        holder.item.apply {
            name.text = "$position  ${cat!!.name}"
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                it()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapter.Holder {
        val binding = CatItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    class Holder(val item: CatItemLayoutBinding): RecyclerView.ViewHolder(item.root)

}
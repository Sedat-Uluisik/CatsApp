package com.sedat.catsapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.sedat.catsapp.R
import com.sedat.catsapp.databinding.CatItemLayoutBinding
import com.sedat.catsapp.model.CatItem
import com.sedat.catsapp.util.Util.IMAGE_URL
import javax.inject.Inject


class FavoriteAdapter @Inject constructor(
    private val glide: RequestManager
): PagingDataAdapter<CatItem, FavoriteAdapter.Holder>(diffUtil()) {

    class diffUtil : DiffUtil.ItemCallback<CatItem>(){
        override fun areItemsTheSame(oldItem: CatItem, newItem: CatItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CatItem, newItem: CatItem): Boolean {
            return oldItem == newItem
        }
    }

    private var onItemClickListener: ((CatItem) -> Unit) ?= null
    fun catItemClick(listener: (CatItem)-> Unit){
        onItemClickListener = listener
    }
    private var onFavoriteBtnClickListener: ((CatItem, ImageView)->Unit) ?= null
    fun favoriteBtnClick(listener: (CatItem, ImageView)-> Unit){
        onFavoriteBtnClickListener = listener
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val cat = getItem(position)

        holder.item.apply {
            name.text = "${cat?.name}"
            if(cat?.image != null){
                if(!cat.image?.url.isNullOrEmpty())
                    glide.load(cat.image?.url).into(image)
                else
                    if (!cat.reference_image_id.isNullOrEmpty())
                        glide.load("$IMAGE_URL/${cat.reference_image_id}.jpg").into(image)
            }
            //search işleminde api, resim ile ilgili herhangi bir bilgi vermiyor, bu nedenle ayriyetten reference_image_id ile resim alınıyor.
            else
                if (!cat?.reference_image_id.isNullOrEmpty())
                    glide.load("$IMAGE_URL/${cat?.reference_image_id}.jpg").into(image)

            favoriteBtnItemLayout.setImageResource(R.drawable.favorite_32)
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                if(cat != null)
                    it(cat)
            }
        }

        holder.item.favoriteBtnItemLayout.setOnClickListener {
            onFavoriteBtnClickListener?.let {
                if(cat != null)
                    it(cat, holder.item.favoriteBtnItemLayout)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = CatItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    class Holder(val item: CatItemLayoutBinding): RecyclerView.ViewHolder(item.root)

}
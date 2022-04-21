package com.example.fragmentvm.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.fragmentvm.databinding.RvItemCatBinding
import com.example.fragmentvm.domain.model.favourite.FavCatDomain
import com.example.fragmentvm.ui.viewholder.FavCatViewHolder

class CatFavouritePagingAdapter(
    private val onFavouriteClicked: (FavCatDomain, Int) -> Unit,
) : PagingDataAdapter<FavCatDomain, FavCatViewHolder>(FavCatComparator) {
    override fun onBindViewHolder(holder: FavCatViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavCatViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        // TODO: Сделать другой layout для списка с избранным
        val binding = RvItemCatBinding.inflate(inflater, parent, false)
        return FavCatViewHolder(binding, onFavouriteClicked)
    }

    companion object {
        private val FavCatComparator = object : DiffUtil.ItemCallback<FavCatDomain>() {
            override fun areItemsTheSame(oldItem: FavCatDomain, newItem: FavCatDomain): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: FavCatDomain, newItem: FavCatDomain): Boolean =
                oldItem == newItem
        }
    }
}
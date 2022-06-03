package com.example.fragmentvm.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.fragmentvm.databinding.RvItemCatBinding
import com.example.fragmentvm.domain.model.favourite.FavCatDomain
import com.example.fragmentvm.ui.utils.VotesEnum
import com.example.fragmentvm.ui.viewholder.FavCatViewHolder

class CatFavouritePagingAdapter(
    private val onVoteClicked: (FavCatDomain, Int, VotesEnum) -> Unit,
    private val onItemClicked: (FavCatDomain) -> Unit,
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
        val binding = RvItemCatBinding.inflate(inflater, parent, false)
        return FavCatViewHolder(binding, onItemClicked, onVoteClicked, onFavouriteClicked)
    }

    fun setToggle(position: Int, vote: VotesEnum) {
        if (
            isVoteDownAgain(position, vote) ||
            isVoteUpAgain(position, vote)
        ) {
            dismissVote(position)
        } else if (vote.value == VotesEnum.UP.value) {
            setVoteUp(position)
        } else if (vote.value == VotesEnum.DOWN.value) {
            setVoteDown(position)
        }
        notifyItemChanged(position)
    }

    private fun isVoteDownAgain(position: Int, vote: VotesEnum): Boolean =
        (vote.value == VotesEnum.DOWN.value && this.snapshot().items[position].isDisliked)

    private fun isVoteUpAgain(position: Int, vote: VotesEnum): Boolean =
        (vote.value == VotesEnum.UP.value && this.snapshot().items[position].isLiked)

    private fun dismissVote(position: Int) {
        this.snapshot().items[position].isDisliked = false
        this.snapshot().items[position].isLiked = false
    }

    private fun setVoteUp(position: Int) {
        this.snapshot().items[position].isLiked = true
        this.snapshot().items[position].isDisliked = false
    }

    private fun setVoteDown(position: Int) {
        this.snapshot().items[position].isLiked = false
        this.snapshot().items[position].isDisliked = true
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

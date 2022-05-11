package com.example.fragmentvm.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.fragmentvm.databinding.RvItemCatBinding
import com.example.fragmentvm.domain.model.cat.CatDomain
import com.example.fragmentvm.ui.utils.VotesEnum
import com.example.fragmentvm.ui.viewholder.CatViewHolder

class CatPagingAdapter(
    private val onVoteClicked: (CatDomain, Int, VotesEnum) -> Unit,
    private val onItemClicked: (CatDomain) -> Unit,
    private val onFavouriteClicked: (CatDomain, Int) -> Unit,
) : PagingDataAdapter<CatDomain, CatViewHolder>(CatComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RvItemCatBinding.inflate(inflater, parent, false)
        return CatViewHolder(binding, onItemClicked, onVoteClicked, onFavouriteClicked)
    }

    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
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

    fun setFavouriteId(position: Int?, favouriteId: Int?) {
        position?.let {
            this.snapshot().items[position].idFavourite = favouriteId
        }
    }

    companion object {
        private val CatComparator = object : DiffUtil.ItemCallback<CatDomain>() {
            override fun areItemsTheSame(oldItem: CatDomain, newItem: CatDomain): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: CatDomain, newItem: CatDomain): Boolean =
                oldItem == newItem
        }
    }
}

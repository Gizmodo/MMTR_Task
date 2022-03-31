package com.example.fragmentvm.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fragmentvm.databinding.RvItemCatBinding
import com.example.fragmentvm.domain.model.cat.CatDomain
import com.example.fragmentvm.ui.utils.VotesEnum

class CatAdapter(
    private val catModels: MutableList<CatDomain>,
    private val onVoteClicked: (CatDomain, Int, VotesEnum) -> Unit,
    private val onItemClicked: (CatDomain) -> Unit,
) : RecyclerView.Adapter<MainViewHolder>() {
    override fun getItemCount() = catModels.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RvItemCatBinding.inflate(inflater, parent, false)

        return MainViewHolder(binding,onItemClicked, onVoteClicked)
    }

    private fun isVoteDownAgain(position: Int, vote: VotesEnum): Boolean =
        (vote.value == VotesEnum.DOWN.value && catModels[position].isDisliked)

    private fun isVoteUpAgain(position: Int, vote: VotesEnum): Boolean =
        (vote.value == VotesEnum.UP.value && catModels[position].isLiked)

    private fun dismissVote(position: Int) {
        catModels[position].isDisliked = false
        catModels[position].isLiked = false
    }

    private fun setVoteUp(position: Int) {
        catModels[position].isLiked = true
        catModels[position].isDisliked = false
    }

    private fun setVoteDown(position: Int) {
        catModels[position].isLiked = false
        catModels[position].isDisliked = true
    }

    fun setToggle(position: Int, vote: VotesEnum) {
        if (isVoteDownAgain(position, vote) || isVoteUpAgain(position, vote)) {
            dismissVote(position)
        } else if (vote.value == VotesEnum.UP.value) {
            setVoteUp(position)
        } else if (vote.value == VotesEnum.DOWN.value) {
            setVoteDown(position)
        }
        notifyItemChanged(position)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val cat = catModels[position]
        holder.bind(cat)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(itemsList: List<CatDomain>) {
        catModels.clear()
        catModels.addAll(itemsList)
        notifyDataSetChanged()
    }
}
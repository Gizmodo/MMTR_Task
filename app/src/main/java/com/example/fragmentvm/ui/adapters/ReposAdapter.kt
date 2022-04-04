package com.example.fragmentvm.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.fragmentvm.databinding.RvItemCatBinding
import com.example.fragmentvm.domain.model.cat.CatDomain
import com.example.fragmentvm.ui.utils.VotesEnum
import com.example.fragmentvm.ui.viewmodels.CatViewHolder

class ReposAdapter(
    private val onVoteClicked: (CatDomain, Int, VotesEnum) -> Unit,
    private val onItemClicked: (CatDomain) -> Unit,
) : PagingDataAdapter<CatDomain, CatViewHolder>(CatComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RvItemCatBinding.inflate(inflater, parent, false)

        return CatViewHolder(binding, onItemClicked, onVoteClicked)
    }

    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
        val repoItem = getItem(position)
        if (repoItem != null) {
            holder.bind(repoItem)
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

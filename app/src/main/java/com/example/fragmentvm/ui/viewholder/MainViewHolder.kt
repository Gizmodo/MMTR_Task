package com.example.fragmentvm.ui.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.fragmentvm.R
import com.example.fragmentvm.core.utils.GlideImpl
import com.example.fragmentvm.databinding.RvItemCatBinding
import com.example.fragmentvm.domain.model.cat.CatDomain
import com.example.fragmentvm.ui.utils.VotesEnum

class MainViewHolder(
    private val binding: RvItemCatBinding,
    private val onItemClicked: (CatDomain) -> Unit,
    private val onVoteClicked: (CatDomain, Int, VotesEnum) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    private val requestOptions = RequestOptions().error(R.drawable.ic_error_placeholder)
    private val transitionOptions = DrawableTransitionOptions().crossFade()
    fun bind(model: CatDomain) {
        with(binding) {
            imgView.setOnClickListener {
                onItemClicked(model)
            }

            with(btnVoteDown) {
                isChecked = model.isDisliked
                setOnClickListener {
                    onVoteClicked(model, adapterPosition, VotesEnum.DOWN)
                }
            }

            with(btnVoteUp) {
                isChecked = model.isLiked
                setOnClickListener {
                    onVoteClicked(model, adapterPosition, VotesEnum.UP)
                }
            }

            Glide.with(itemView.context)
                .addDefaultRequestListener(
                    GlideImpl.OnCompleted {
                        setProgressBarVisibility(View.GONE)
                    }
                )
                .applyDefaultRequestOptions(requestOptions)
                .load(model.url)
                .thumbnail()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(transitionOptions)
                .into(imgView)
            setProgressBarVisibility(View.VISIBLE)
        }
    }

    private fun setProgressBarVisibility(state: Int) {
        binding.itemProgressBar.visibility = state
    }
}

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
import com.example.fragmentvm.domain.model.favourite.FavCatDomain

class FavCatViewHolder(
    private val binding: RvItemCatBinding,
    private val onFavouriteClicked: (FavCatDomain, Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    private val requestOptions = RequestOptions().error(R.drawable.ic_error_placeholder)
    private val transitionOptions = DrawableTransitionOptions().crossFade()
    fun bind(model: FavCatDomain) {
        with(binding) {

            btnFavourite.isChecked = true
            btnFavourite.setOnClickListener {
                onFavouriteClicked(model, adapterPosition)
            }

            Glide.with(itemView.context)
                .addDefaultRequestListener(GlideImpl.OnCompleted {
                    setProgressBarVisibility(View.GONE)
                })
                .applyDefaultRequestOptions(requestOptions)
                .load(model.image_url)
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
package com.example.fragmentvm.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.fragmentvm.R
import com.example.fragmentvm.databinding.RvItemCatBinding
import com.example.fragmentvm.model.Cat
import com.example.fragmentvm.utils.GlideImpl
import com.example.fragmentvm.utils.VotesEnum

class CatAdapter(
    private val cats: List<Cat>,
    private val onVoteClickListener: (
        cat: Cat,
        position: Int,
        vote: VotesEnum,
    ) -> Unit,
    private val onClickListener: (
        cat: Cat,
    ) -> Unit,
) :
    RecyclerView.Adapter<CatAdapter.MainViewHolder>() {

    override fun getItemCount() = cats.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RvItemCatBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    fun setToggle(position: Int, vote: VotesEnum) {
        if (
            (vote.value == VotesEnum.DOWN.value && cats[position].isDisliked) ||
            (vote.value == VotesEnum.UP.value && cats[position].isLiked)
        ) {
            cats[position].isDisliked = false
            cats[position].isLiked = false
        } else if (vote.value == VotesEnum.UP.value) {
            cats[position].isLiked = true
            cats[position].isDisliked = false
        } else if (vote.value == VotesEnum.DOWN.value) {
            cats[position].isDisliked = true
            cats[position].isLiked = false
        }
        notifyItemChanged(position)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val cat = cats[position]
        holder.bind(cat)
    }

    inner class MainViewHolder(private val binding: RvItemCatBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: Cat) {
            with(binding) {
                setProgressBarVisibility(View.VISIBLE)
                Glide
                    .with(itemView.context)
                    .addDefaultRequestListener(GlideImpl.OnCompleted {
                        setProgressBarVisibility(View.GONE)
                    })
                    .applyDefaultRequestOptions(RequestOptions().error(R.drawable.ic_error_placeholder))
                    .load(model.url)
                    .thumbnail()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transition(DrawableTransitionOptions().crossFade())
                    .into(imgView)

                with(btnVoteDown) {
                    isChecked = model.isDisliked
                    setOnClickListener {
                        onVoteClickListener(model, adapterPosition, VotesEnum.DOWN)
                    }
                }

                with(btnVoteUp) {
                    isChecked = model.isLiked
                    setOnClickListener {
                        onVoteClickListener(model, adapterPosition, VotesEnum.UP)
                    }
                }

                imgView.setOnClickListener {
                    onClickListener(model)
                }

            }
        }

        private fun setProgressBarVisibility(state: Int) {
            binding.itemProgressBar.visibility = state
        }
    }
}
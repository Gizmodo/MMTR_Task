package com.example.fragmentvm.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.fragmentvm.R
import com.example.fragmentvm.core.utils.GlideImpl
import com.example.fragmentvm.databinding.RvItemCatBinding
import com.example.fragmentvm.model.cat.CatModel
import com.example.fragmentvm.model.vote.VotesEnum

class CatAdapter(
    private val catModels: MutableList<CatModel>,
    private val onVoteClickListener: (
        catModel: CatModel,
        position: Int,
        vote: VotesEnum,
    ) -> Unit,
    private val onClickListener: (CatModel) -> Unit,
) :
    RecyclerView.Adapter<CatAdapter.MainViewHolder>() {
    private val requestOptions = RequestOptions().error(R.drawable.ic_error_placeholder)
    private val transitionOptions = DrawableTransitionOptions().crossFade()
    override fun getItemCount() = catModels.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RvItemCatBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
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
    fun updateList(itemsList: List<CatModel>) {
        catModels.clear()
        catModels.addAll(itemsList)
        notifyDataSetChanged()
    }

    inner class MainViewHolder(private val binding: RvItemCatBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: CatModel) {
            with(binding) {
                setProgressBarVisibility(View.VISIBLE)
                Glide
                    .with(itemView.context)
                    .addDefaultRequestListener(GlideImpl.OnCompleted {
                        setProgressBarVisibility(View.GONE)
                    })
                    .applyDefaultRequestOptions(requestOptions)
                    .load(model.url)
                    .thumbnail()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transition(transitionOptions)
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
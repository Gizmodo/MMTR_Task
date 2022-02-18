package com.example.fragmentvm.adapter

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
import com.example.fragmentvm.databinding.RvItemCatBinding
import ru.mmtr.domain.model.Cat

class CatAdapter(
    private val cats: MutableList<Cat>,
    private val onVoteClickListener: (
        cat: Cat,
        position: Int,
        vote: ru.mmtr.domain.utils.VotesEnum,
    ) -> Unit,
    private val onClickListener: (Cat) -> Unit,
) :
    RecyclerView.Adapter<CatAdapter.MainViewHolder>() {
    private val requestOptions = RequestOptions().error(R.drawable.ic_error_placeholder)
    private val transitionOptions = DrawableTransitionOptions().crossFade()
    override fun getItemCount() = cats.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RvItemCatBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    private fun isVoteDownAgain(position: Int, vote: ru.mmtr.domain.utils.VotesEnum): Boolean =
        (vote.value == ru.mmtr.domain.utils.VotesEnum.DOWN.value && cats[position].isDisliked)

    private fun isVoteUpAgain(position: Int, vote: ru.mmtr.domain.utils.VotesEnum): Boolean =
        (vote.value == ru.mmtr.domain.utils.VotesEnum.UP.value && cats[position].isLiked)

    private fun dismissVote(position: Int) {
        cats[position].isDisliked = false
        cats[position].isLiked = false
    }

    private fun setVoteUp(position: Int) {
        cats[position].isLiked = true
        cats[position].isDisliked = false
    }

    private fun setVoteDown(position: Int) {
        cats[position].isLiked = false
        cats[position].isDisliked = true
    }

    fun setToggle(position: Int, vote: ru.mmtr.domain.utils.VotesEnum) {
        if (isVoteDownAgain(position, vote) || isVoteUpAgain(position, vote)) {
            dismissVote(position)
        } else if (vote.value == ru.mmtr.domain.utils.VotesEnum.UP.value) {
            setVoteUp(position)
        } else if (vote.value == ru.mmtr.domain.utils.VotesEnum.DOWN.value) {
            setVoteDown(position)
        }
        notifyItemChanged(position)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val cat = cats[position]
        holder.bind(cat)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(itemsList: List<Cat>) {
        cats.clear()
        cats.addAll(itemsList)
        notifyDataSetChanged()
    }

    inner class MainViewHolder(private val binding: RvItemCatBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: Cat) {
            with(binding) {
                setProgressBarVisibility(View.VISIBLE)
                Glide
                    .with(itemView.context)
                    .addDefaultRequestListener(ru.mmtr.domain.utils.GlideImpl.OnCompleted {
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
                        onVoteClickListener(model, adapterPosition, ru.mmtr.domain.utils.VotesEnum.DOWN)
                    }
                }

                with(btnVoteUp) {
                    isChecked = model.isLiked
                    setOnClickListener {
                        onVoteClickListener(model, adapterPosition, ru.mmtr.domain.utils.VotesEnum.UP)
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
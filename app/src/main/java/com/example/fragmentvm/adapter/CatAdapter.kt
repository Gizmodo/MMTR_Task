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
import timber.log.Timber

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
        cats[position].state = vote.value as Boolean
        notifyItemChanged(position)
    }

    fun changeText(position: Int, value: Int) {
        Timber.d("Before")
        Timber.i(cats[position].toString())
        cats[position].height = value
        notifyItemChanged(position)
        Timber.d("After")
        Timber.i(cats[position].toString())
        //holder.binding.tvTest.text = text
        //notifyItemChanged(holder.adapterPosition)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val cat = cats[position]
        holder.bind(cat)
        /* holder.binding.switch1.setOnCheckedChangeListener { view, isChecked ->
             Timber.d("Switch state $isChecked")
             cats[holder.adapterPosition].state = isChecked
 //            notifyItemChanged(holder.adapterPosition)
         }*/
    }

    companion object {
        val ro = RequestOptions()
            .error(R.drawable.ic_error_placeholder)
    }

    inner class MainViewHolder(private val binding: RvItemCatBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /* init {
             itemView.setOnClickListener {
                 onVoteClickListener(it, cats[adapterPosition], adapterPosition, VotesEnum.UNDEFINED)
             }
         }*/

        private fun setProgressBarVisibility(state: Int) {
            binding.itemProgressBar.visibility = state
        }

        fun bind(model: Cat) {
            with(binding) {
                setProgressBarVisibility(View.VISIBLE)
                Glide
                    .with(itemView.context)
                    .addDefaultRequestListener(GlideImpl.OnCompleted {
                        setProgressBarVisibility(View.GONE)
                    })
                    .applyDefaultRequestOptions(ro)
                    .load(model.url)
                    .thumbnail(0.5f)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transition(DrawableTransitionOptions().crossFade())
                    .into(imgView)

                toggleVote.clearChecked()

                with(btnVoteDown) {
                    tag = model
                    isChecked = model.state
                    setOnClickListener {
                        onVoteClickListener(model, adapterPosition, VotesEnum.DOWN)
                    }
                }

                with(btnVoteUp) {
                    tag = model
                    isChecked = model.state
                    setOnClickListener {
                        onVoteClickListener(model, adapterPosition, VotesEnum.UP)
                    }
                }

                imgView.setOnClickListener {
                    onClickListener(model)
                }

            }
        }
    }
}
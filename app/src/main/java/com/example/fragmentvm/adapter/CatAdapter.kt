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
import com.example.fragmentvm.databinding.RecyclerviewItemCatBinding
import com.example.fragmentvm.model.Cat
import com.example.fragmentvm.utils.GlideImpl
import com.example.fragmentvm.utils.VotesEnum
import timber.log.Timber


class CatAdapter(
    private val cats: List<Cat>,
    private val listener: OnRecyclerViewItemClick,
    private val voteListener: OnVoteClickListener,
) :
    RecyclerView.Adapter<CatAdapter.MainViewHolder>() {
    override fun getItemCount() = cats.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = RecyclerviewItemCatBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    private fun showProgress(holder: MainViewHolder) {
        holder.binding.itemProgressBar.visibility = View.VISIBLE
    }

    private fun hideProgress(holder: MainViewHolder) {
        holder.binding.itemProgressBar.visibility = View.GONE
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val cat = cats[position]

        val requestOptions = RequestOptions()
            .error(R.drawable.ic_error_placeholder)

        showProgress(holder)

        Glide
            .with(holder.itemView.context)
            .addDefaultRequestListener(GlideImpl.OnCompleted {
                hideProgress(holder)
            })
            .applyDefaultRequestOptions(requestOptions)
            .load(cat.url)
            .thumbnail(0.5f)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .transition(DrawableTransitionOptions().crossFade())
            .into(holder.binding.imgView)

        holder.binding.imgView.setOnClickListener {
            listener.onRecyclerViewItemClick(it, cat)
        }

        holder.binding.btnThumbDown.setOnClickListener {
            Timber.d("VoteDown")
            voteListener.onVoteClickListener(it, cat, VotesEnum.DOWN)
        }
        holder.binding.btnThumbUp.setOnClickListener {
            Timber.d("VoteUp")
            voteListener.onVoteClickListener(it, cat, VotesEnum.UP)
        }
    }

    inner class MainViewHolder(val binding: RecyclerviewItemCatBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface OnRecyclerViewItemClick {
        fun onRecyclerViewItemClick(view: View, cat: Cat)
    }

    interface OnVoteClickListener {
        fun onVoteClickListener(view: View, cat: Cat, vote: VotesEnum)
    }

}
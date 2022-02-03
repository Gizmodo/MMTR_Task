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
import com.google.android.material.button.MaterialButtonToggleGroup
import io.github.serpro69.kfaker.Faker
import timber.log.Timber


class CatAdapter(
    private val cats: List<Cat>,
    private val listener: OnRecyclerViewItemClick,
    private val voteListener: OnVoteClickListener,
    private val groupListener: OnButtonCheckedListener,
    private val onDotsListener: OnDotsListener,
) :
    RecyclerView.Adapter<CatAdapter.MainViewHolder>() {
    val faker = Faker()
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

    fun setToggle(position: Int, state: Boolean) {
        cats[position].state = state
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

        val requestOptions = RequestOptions()
            .error(R.drawable.ic_error_placeholder)

        showProgress(holder)
        holder.binding.tvTest.text = cat.height.toString()
        holder.binding.switch1.isChecked = cat.state
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

        holder.binding.tvTest.setOnClickListener {
            //Timber.d(cats[holder.adapterPosition].toString())
            val num = faker.random.nextInt(1..180)
            holder.binding.tvTest.text = num.toString()
            cats[holder.adapterPosition].height = num
        }

        holder.binding.imgView.setOnClickListener {
            listener.onRecyclerViewItemClick(it, cat)
        }

        holder.binding.btnVoteDown.setOnClickListener {
            Timber.d("VoteDown")
            voteListener.onVoteClickListener(it, cat, VotesEnum.DOWN)
        }
        holder.binding.btnVoteUp.setOnClickListener {
            Timber.d("VoteUp")
            voteListener.onVoteClickListener(it, cat, VotesEnum.UP)
        }

        holder.binding.toggleVote.setOnClickListener {
            Timber.d("toggleVote")
            groupListener.onButtonChecked(holder.binding.toggleVote,
                cat)
        }

        holder.binding.switch1.setOnCheckedChangeListener { view, isChecked ->
            Timber.d("Switch state $isChecked")
            cats[holder.adapterPosition].state = isChecked
//            notifyItemChanged(holder.adapterPosition)
        }
        holder.binding.btnDots.setOnClickListener {
            onDotsListener.onClick(it, cat, position)
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

    interface OnDotsListener {
        fun onClick(view: View, cat: Cat, position: Int)
    }

    interface OnButtonCheckedListener {
        fun onButtonChecked(
            group: MaterialButtonToggleGroup, cat: Cat,
        )
    }
}
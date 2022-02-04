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
import com.google.android.material.button.MaterialButtonToggleGroup
import timber.log.Timber

class CatAdapter(
    private val cats: List<Cat>,
    private val listener: OnRecyclerViewItemClick,
    private val voteListener: OnVoteClickListener,
    private val groupListener: OnButtonCheckedListener,
    private val onDotsListener: OnDotsListener,
    private val onVoteClickListener: (
        view: View,
        cat: Cat,
        position: Int,
        state: VotesEnum,
    ) -> Unit,
) :
    RecyclerView.Adapter<CatAdapter.MainViewHolder>() {
    override fun getItemCount() = cats.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = RvItemCatBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
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
        holder.bind(cat)
        holder.itemView.setOnClickListener {
            when (it.id) {
                R.id.btnVoteDown -> onVoteClickListener(it, cat, position, VotesEnum.DOWN)
                R.id.btnVoteUp -> onVoteClickListener(it, cat, position, VotesEnum.UP)
            }
        }
        /* holder.binding.tvTest.setOnClickListener {
             //Timber.d(cats[holder.adapterPosition].toString())
             val num = faker.random.nextInt(1..180)
             holder.binding.tvTest.text = num.toString()
             cats[holder.adapterPosition].height = num
         }
 */


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

        init {
            itemView.setOnClickListener {
                onVoteClickListener(it, cats[adapterPosition], adapterPosition, VotesEnum.UNDEFINED)
            }
        }

        private fun setProgressBarVisibility(state: Int) {
            binding.itemProgressBar.visibility = state
        }

        fun bind(model: Cat) {
            with(binding) {
                setProgressBarVisibility(View.VISIBLE)
                Glide
                    .with(itemView.context)
//                .with(this@MainViewHolder.itemView.context)
                    .addDefaultRequestListener(GlideImpl.OnCompleted {
                        setProgressBarVisibility(View.GONE)
//                    hideProgress(holder)
                    })
                    .applyDefaultRequestOptions(ro)
                    .load(model.url)
                    .thumbnail(0.5f)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transition(DrawableTransitionOptions().crossFade())
                    .into(imgView)

                toggleVote.clearChecked()
                //Assign model to tag
                btnVoteDown.tag = model
                btnVoteUp.tag = model

                //Click listeners
                btnVoteUp.setOnClickListener {
                    onVoteClickListener(it, model, adapterPosition, VotesEnum.UP)
                }

                btnVoteDown.setOnClickListener {
                    onVoteClickListener(it, model, adapterPosition, VotesEnum.DOWN)
                }










                btnVoteUp.isChecked = model.state
                btnVoteDown.isChecked = model.state

                imgView.setOnClickListener {
                    listener.onRecyclerViewItemClick(it, model)
                }


                /*   btnVoteDown.setOnClickListener {
                       Timber.d("VoteDown")
                       voteListener.onVoteClickListener(it, model, VotesEnum.DOWN)
                   }
                   btnVoteUp.setOnClickListener {
                       Timber.d("VoteUp")
                       voteListener.onVoteClickListener(it, model, VotesEnum.UP)
                   }

                   toggleVote.setOnClickListener {
                       Timber.d("toggleVote")
                       groupListener.onButtonChecked(toggleVote, model)
                   }

                   btnDots.setOnClickListener {
                       // TODO: adapterPosition???
                       onDotsListener.onClick(it, model, adapterPosition)
                   }*/
            }
        }
    }

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
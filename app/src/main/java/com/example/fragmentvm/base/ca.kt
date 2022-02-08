package com.example.fragmentvm.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.fragmentvm.R
import com.example.fragmentvm.databinding.RvItemCatBinding
import com.example.fragmentvm.model.Cat
import com.example.fragmentvm.utils.GlideImpl

class ca : BaseAdapter<Cat>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Cat> {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RvItemCatBinding.inflate(inflater, parent, false)
        return CatViewHolder(binding)
    }
    var callback: BaseAdapterCallback<Cat>? = null
    private inner class CatViewHolder(private val binding: RvItemCatBinding) :
        BaseAdapter.BaseViewHolder<Cat>(binding.root) {
        val imgView2: AppCompatImageView = itemView.findViewById(R.id.imgView)

        override fun bind(model: Cat) {
            imgView2.setOnClickListener {
                callback?.onItemClick(model, it)
//                    onClickListener(model)
            }
            with(binding) {
                Glide
                    .with(itemView.context)
                    .addDefaultRequestListener(GlideImpl.OnCompleted {
                        setProgressBarVisibility(View.GONE)
                    })
                    .applyDefaultRequestOptions(RequestOptions().error(R.drawable.ic_error_placeholder))
                    .load(model.url)
                    .thumbnail(0.5f)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transition(DrawableTransitionOptions().crossFade())
                    .into(imgView)

                /*with(btnVoteDown) {
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
                }*/


            }
        }

        private fun setProgressBarVisibility(state: Int) {
            binding.itemProgressBar.visibility = state
        }
    }
}



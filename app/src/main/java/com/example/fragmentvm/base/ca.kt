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
import com.example.fragmentvm.model.Cat

class ca : BaseAdapter<Cat>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Cat> {
        return CatViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.rv_item_cat, parent, false)
        )
    }

    private inner class CatViewHolder(view: View) : BaseAdapter.BaseViewHolder<Cat>(view) {
        override fun bind(model: Cat) {
//            itemView.findViewById<Button>(R.id.btnDots)
//            setProgressBarVisibility(View.VISIBLE)
            Glide
                .with(itemView.context)
                /*   .addDefaultRequestListener(GlideImpl.OnCompleted {
                       setProgressBarVisibility(View.GONE)
                   })
                */.applyDefaultRequestOptions(RequestOptions().error(R.drawable.ic_error_placeholder))
                .load(model.url)
                .thumbnail(0.5f)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(DrawableTransitionOptions().crossFade())
                .into(itemView.findViewById<AppCompatImageView>(R.id.imgView))
        }

    }
}
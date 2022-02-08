package com.example.fragmentvm.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.fragmentvm.R
import com.example.fragmentvm.model.Cat
import com.example.fragmentvm.utils.GlideImpl
import com.google.android.material.progressindicator.CircularProgressIndicator

class Ca : BaseAdapter<Cat>() {

    var callback: BaseAdapterCallback<Cat>? = null
    private var clickHandler: ServiceClickHandler? = null

    interface ServiceClickHandler {
        fun onPriceClick(model: Cat, view: View, position: Int)
    }

    fun attachClick(clickHandler: ServiceClickHandler) {
        this.clickHandler = clickHandler
    }

    fun attachClickBase(click: BaseAdapterCallback<Cat>) {
        this.callback = click
    }

    fun detachClick() {
        this.clickHandler = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Cat> {
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_item_cat, parent, false))
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder<Cat>(itemView = itemView) {
        val imgCat: AppCompatImageView = itemView.findViewById(R.id.imgView)
        val btnDots: Button = itemView.findViewById<Button>(R.id.btnDots)

        private val itemProgressBar: CircularProgressIndicator =
            itemView.findViewById(R.id.itemProgressBar)

        private fun setProgressBarVisibility(state: Int) {
            itemProgressBar.visibility = state
        }

        override fun bind(model: Cat) {
            imgCat.setOnClickListener {
                clickHandler?.onPriceClick(model = mDataList[adapterPosition],
                    view = it,
                    position = adapterPosition)
            }
            btnDots.setOnClickListener {
                callback?.onItemClick(mDataList[adapterPosition], it, adapterPosition)

                /*clickHandler?.onPriceClick(model = mDataList[adapterPosition],
                    view = it,
                    position = adapterPosition)*/
            }
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
                .into(imgCat)
        }
    }
}



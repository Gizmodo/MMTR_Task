package com.example.fragmentvm.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.fragmentvm.databinding.RecyclerviewItemCatBinding
import com.example.fragmentvm.model.Cat
import com.example.fragmentvm.utils.GlideImpl


class CatAdapter(
    private val cats: List<Cat>,
    private val listener: OnRecyclerViewItemClick,
) :
    RecyclerView.Adapter<CatAdapter.MainViewHolder>() {
    override fun getItemCount() = cats.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = RecyclerviewItemCatBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val cat = cats[position]

        val requestOptions = RequestOptions()
            .error(ColorDrawable(Color.RED))

        holder.binding.itemProgressBar.visibility = View.VISIBLE

        Glide
            .with(holder.itemView.context)
            .addDefaultRequestListener(GlideImpl.OnCompleted {
                holder.binding.itemProgressBar.visibility = View.GONE
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
    }


    inner class MainViewHolder(val binding: RecyclerviewItemCatBinding) :
        RecyclerView.ViewHolder(binding.root)


    interface OnRecyclerViewItemClick {
        fun onRecyclerViewItemClick(view: View, cat: Cat)
    }


}
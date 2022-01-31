package com.example.fragmentvm.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import androidx.swiperefreshlayout.widget.CircularProgressDrawable.DEFAULT
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.fragmentvm.R
import com.example.fragmentvm.databinding.RecyclerviewItemCatBinding
import com.example.fragmentvm.model.Cat


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
        val circularProgressDrawable = CircularProgressDrawable(holder.itemView.context)
        circularProgressDrawable.apply {
            setStyle(DEFAULT)
            setColorSchemeColors(R.color.error)
            start()
        }

        val requestOptions = RequestOptions()
        requestOptions.centerCrop()
        requestOptions.placeholder(circularProgressDrawable)
        requestOptions.error(R.drawable.ic_outline_running_with_errors_24)
        requestOptions.fitCenter()

        Glide
            .with(holder.itemView.context)
            .load(cat.url)
            .apply(requestOptions)
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
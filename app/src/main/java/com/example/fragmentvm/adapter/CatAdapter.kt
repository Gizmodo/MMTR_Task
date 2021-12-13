package com.example.fragmentvm.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fragmentvm.databinding.RecyclerviewItemCatBinding
import com.example.fragmentvm.model.Cat

class CatAdapter(
    private val cats: List<Cat>,
    private val listener: onRecyclerViewItemClick
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
        Glide.with(holder.itemView.context).load(cat.url).into(holder.binding.imgView)
//        holder.itemView.setOnClickListener { onItemClickListener.onClick(cat) }
        holder.binding.imgView.setOnClickListener {
            listener.onRecyclerViewItemClick(it, cat)
        }
    }


    inner class MainViewHolder(val binding: RecyclerviewItemCatBinding) :
        RecyclerView.ViewHolder(binding.root)


    interface onRecyclerViewItemClick {
        fun onRecyclerViewItemClick(view: View, cat: Cat)
    }


}
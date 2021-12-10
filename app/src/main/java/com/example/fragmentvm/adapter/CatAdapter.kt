package com.example.fragmentvm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fragmentvm.databinding.RecyclerviewItemCatBinding
import com.example.fragmentvm.model.Cat

class CatAdapter(private val onItemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<CatAdapter.MainViewHolder>() {
    inner class MainViewHolder(val binding: RecyclerviewItemCatBinding) :
        RecyclerView.ViewHolder(binding.root)

    var cats = mutableListOf<Cat>()
    fun setCatsList(cats: List<Cat>) {
        this.cats = cats.toMutableList()
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onClick(item: Cat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = RecyclerviewItemCatBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val cat = cats[position]
        Glide.with(holder.itemView.context).load(cat.url).into(holder.binding.imgView)
        holder.itemView.setOnClickListener { onItemClickListener.onClick(cat) }
    }

    override fun getItemCount(): Int {
        return cats.size
    }
}
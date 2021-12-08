package com.example.fragmentvm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fragmentvm.databinding.AdapterCatBinding
import com.example.fragmentvm.model.Cat

class MainAdapter(private val onItemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<MainAdapter.MainViewHolder>() {
    inner class MainViewHolder(val binding: AdapterCatBinding) :
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

        val binding = AdapterCatBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val cat = cats[position]
        holder.binding.originalFilename.text = "${cat.width} x ${cat.height}"
        Glide.with(holder.itemView.context).load(cat.url).into(holder.binding.imageview)

        holder.itemView.setOnClickListener { onItemClickListener.onClick(cat) }
    }

    override fun getItemCount(): Int {
        return cats.size
    }
}
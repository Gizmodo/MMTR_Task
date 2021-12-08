package com.example.fragmentvm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fragmentvm.databinding.AdapterCatBinding
import com.example.fragmentvm.model.Cat
import timber.log.Timber

class MainAdapter : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {
    inner class MainViewHolder(val bindig: AdapterCatBinding) :
        RecyclerView.ViewHolder(bindig.root) {

    }

    var cats = mutableListOf<Cat>()
    fun setCatsList(cats: List<Cat>) {
        this.cats = cats.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = AdapterCatBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val cat = cats[position]
        Timber.d(cat.toString())
        holder.bindig.originalFilename.text = "${cat.width} x ${cat.height}"
        Glide.with(holder.itemView.context).load(cat.url).into(holder.bindig.imageview)
    }

    override fun getItemCount(): Int {
        return cats.size
    }
}
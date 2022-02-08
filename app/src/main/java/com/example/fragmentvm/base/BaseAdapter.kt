package com.example.fragmentvm.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView


abstract class BaseAdapter<P> : RecyclerView.Adapter<BaseAdapter.BaseViewHolder<P>>() {

    protected open var mDataList: MutableList<P> = ArrayList()
    protected var mCallback: BaseAdapterCallback<P>? = null
    var hasItems = false

    override fun onBindViewHolder(holder: BaseViewHolder<P>, position: Int) {
        holder.bind(mDataList[position])
        holder.itemView.setOnClickListener {
            mCallback?.onItemClick(mDataList[position], holder.itemView)
        }
        holder.itemView.setOnLongClickListener {
            if (mCallback == null) {
                false
            } else {
                mCallback!!.onLongClick(mDataList[position], holder.itemView)
            }

        }
    }

    override fun getItemCount(): Int {
        return mDataList.count()
    }

    fun attachCallback(callback: BaseAdapterCallback<P>) {
        this.mCallback = callback
    }

    fun addList(dataList: List<P>) {
        mDataList.addAll(dataList)
        hasItems = true
        notifyDataSetChanged()
    }

    fun addItem(newItem: P) {
        mDataList.add(newItem)
        notifyItemInserted(mDataList.size - 1)
    }

    fun addItemToTop(newItem: P) {
        mDataList.add(0, newItem)
        notifyItemInserted(0)
    }

    fun addListToTop(dataList: List<P>) {
        mDataList.addAll(if (mDataList.isEmpty()) 0 else mDataList.size - 1, dataList)
        notifyDataSetChanged()
    }

    fun clear() {
        mDataList.clear()
        hasItems = false
        notifyDataSetChanged()
    }

    fun updateItems(itemsList: List<P>) {
        mDataList.clear()
        addList(itemsList)
    }

    fun updateItem(position: Int, item: P) {
        mDataList[position] = item
        notifyItemChanged(position)
    }

    fun removeItem(item: P) {
        val p = mDataList.indexOf(item)
        if (p != -1) {
            mDataList.removeAt(p)
            notifyItemRemoved(p)
        }
    }

    fun getData() = mDataList

    abstract class BaseViewHolder<T>(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        abstract fun bind(model: T)
    }

    interface BaseAdapterCallback<T> {
        fun onItemClick(model: T, view: View)
        fun onLongClick(model: T, view: View): Boolean
    }
}
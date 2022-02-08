package com.example.fragmentvm.base

import androidx.recyclerview.widget.RecyclerView


abstract class BaseAdapter<T> : RecyclerView.Adapter<BaseViewHolder<T>>() {

    protected open var mDataList: MutableList<T> = ArrayList()
    protected var mCallback: BaseAdapterCallback<T>? = null
    var hasItems = false

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        holder.bind(mDataList[position])

       /* holder.itemView.setOnClickListener {
            mCallback?.onItemClick(mDataList[position], holder.itemView, position)
        }*/
    }

    override fun getItemCount(): Int {
        return mDataList.count()
    }

    fun attachCallback(callback: BaseAdapterCallback<T>) {
        this.mCallback = callback
    }

    fun addList(dataList: List<T>) {
        mDataList.addAll(dataList)
        hasItems = true
        notifyDataSetChanged()
    }

    fun addItem(newItem: T) {
        mDataList.add(newItem)
        notifyItemInserted(mDataList.size - 1)
    }

    fun addItemToTop(newItem: T) {
        mDataList.add(0, newItem)
        notifyItemInserted(0)
    }

    fun addListToTop(dataList: List<T>) {
        mDataList.addAll(if (mDataList.isEmpty()) 0 else mDataList.size - 1, dataList)
        notifyDataSetChanged()
    }

    fun clear() {
        mDataList.clear()
        hasItems = false
        notifyDataSetChanged()
    }

    fun updateItems(itemsList: List<T>) {
        mDataList.clear()
        addList(itemsList)
    }

    fun updateItem(position: Int, item: T) {
        mDataList[position] = item
        notifyItemChanged(position)
    }

    fun removeItem(item: T) {
        val p = mDataList.indexOf(item)
        if (p != -1) {
            mDataList.removeAt(p)
            notifyItemRemoved(p)
        }
    }

    fun getData() = mDataList
}
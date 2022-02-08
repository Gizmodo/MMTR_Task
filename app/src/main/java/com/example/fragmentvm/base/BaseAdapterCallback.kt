package com.example.fragmentvm.base

import android.view.View

interface BaseAdapterCallback<T> {
    fun onItemClick(model: T, view: View, position: Int)
}
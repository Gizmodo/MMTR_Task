package com.example.fragmentvm.core.utils

import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding

fun Fragment.navigateExt(directions: NavDirections) {
    view?.let { Navigation.findNavController(it).navigate(directions) }
}

fun Fragment.navigateExt(@IdRes resId: Int) {
    view?.let { Navigation.findNavController(it).navigate(resId) }
}

fun <T : ViewBinding> viewBindingWithBinder(binder: (View) -> T) =
    FragmentAutoClearedBinding(binder)

fun FragmentActivity.findFragmentContainerNavController(@IdRes host: Int): NavController {
    try {
        val navHostFragment = supportFragmentManager.findFragmentById(host) as NavHostFragment
        return navHostFragment.findNavController()
    } catch (e: Exception) {
        throw IllegalStateException("Activity $this does not have a NavController set on $host")
    }
}

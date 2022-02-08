package com.example.fragmentvm.base

import android.view.View
import android.widget.AdapterView
import android.widget.DatePicker
import android.widget.Spinner
import androidx.appcompat.widget.ListPopupWindow
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*


fun showErrorSnackbar(v: View, t: Throwable) =
    Snackbar.make(v, "Error: $t", Snackbar.LENGTH_LONG).show()

fun Spinner.limitDropdownHeight(limit: Int = 750) = try {
    val popup = this.javaClass.getDeclaredField("mPopup")
    popup.isAccessible = true

    val popipWindow = popup.get(this) as ListPopupWindow
    popipWindow.height = limit
} catch (e: Exception) {
    e.printStackTrace()
}

fun <T> Spinner.onItemSelected(callback: (item: T) -> Unit) {
    this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(p0: AdapterView<*>?) {}

        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            callback.invoke(p0?.selectedItem as T)
        }
    }
}

fun DatePicker.getTimeInMil(): Long {
    val c = Calendar.getInstance()
    c.set(Calendar.YEAR, this.year)
    c.set(Calendar.MONTH, this.month)
    c.set(Calendar.DAY_OF_MONTH, this.dayOfMonth)

    return c.timeInMillis
}

fun Long.getYearFromMil(): String {
    val format = SimpleDateFormat("yyyy")
    return format.format(this)
}
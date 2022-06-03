package com.example.fragmentvm.core.utils

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.shashank.sony.fancytoastlib.FancyToast

inline fun Fragment.fancyException(message: () -> String): Toast = FancyToast
    .makeText(this.context, message(), FancyToast.LENGTH_LONG, FancyToast.ERROR, false)
    .apply {
        show()
    }

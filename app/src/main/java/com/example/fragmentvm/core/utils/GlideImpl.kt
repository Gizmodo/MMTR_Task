package com.example.fragmentvm.core.utils

import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

object GlideImpl {

    object OnCompleted : RequestListener<Any> {

        private lateinit var onComplete: () -> Unit

        operator fun invoke(onComplete: () -> Unit): OnCompleted {
            OnCompleted.onComplete = { onComplete() }
            return this
        }

        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Any>?,
            isFirstResource: Boolean,
        ): Boolean {
            onComplete()
            return false
        }

        override fun onResourceReady(
            resource: Any?,
            model: Any?,
            target: Target<Any>?,
            dataSource: DataSource?,
            isFirstResource: Boolean,
        ): Boolean {
            onComplete()
            return false
        }
    }
}

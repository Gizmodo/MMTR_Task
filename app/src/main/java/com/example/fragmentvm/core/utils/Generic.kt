package com.example.fragmentvm.core.utils

class Generic<T : Any>(val klass: Class<T>) {
    companion object {
        inline operator fun <reified T : Any> invoke() = Generic(T::class.java)
    }

    fun checkType(t: Any) = klass.isAssignableFrom(t.javaClass)
}
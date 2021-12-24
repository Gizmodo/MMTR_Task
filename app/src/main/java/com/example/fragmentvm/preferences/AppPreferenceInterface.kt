package com.example.fragmentvm.preferences

interface AppPreferenceInterface {
    fun getEmail(): String
    fun setEmail(email: String)
    var apikey: String
}
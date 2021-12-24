package com.example.fragmentvm.repository

import android.content.Context
import com.example.fragmentvm.preferences.AppPreferenceInterface
import javax.inject.Inject


class RepositoryPrefs @Inject constructor(context: Context) : AppPreferenceInterface {
    companion object {
        const val TOKEN_EMAIL = "email"
        const val TOKEN_DESCRIPTION = "description"
        const val TOKEN_APIKEY = "apikey"
    }

    private var preference = context.getSharedPreferences("dagger-pref", Context.MODE_PRIVATE)
    private var editor = preference.edit()

    private fun setString(key: String, value: String) {
        editor.putString(key, value).apply()
    }

    private fun getString(key: String, defaultValue: String = ""): String {
        return preference.getString(key, defaultValue) ?: defaultValue
    }

    override fun getEmail(): String = getString(TOKEN_EMAIL)

    override fun setEmail(email: String) {
        setString(TOKEN_EMAIL, email)
    }

    override var apikey: String
        get() = getString(TOKEN_APIKEY)
        set(value) {
            setString(TOKEN_APIKEY, value)
        }
}
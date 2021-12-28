package com.example.fragmentvm.repository

import android.content.Context
import com.example.fragmentvm.preferences.SharedPreferenceInterface
import javax.inject.Inject


class RepositorySharedPref @Inject constructor(context: Context) : SharedPreferenceInterface {
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

    override var description: String
        get() = getString(TOKEN_DESCRIPTION)
        set(value) {
            setString(TOKEN_DESCRIPTION, value)
        }
    override var email: String
        get() = getString(TOKEN_EMAIL)
        set(value) {
            setString(TOKEN_EMAIL, value)
        }
    override var apikey: String
        get() = getString(TOKEN_APIKEY)
        set(value) {
            setString(TOKEN_APIKEY, value)
        }
}
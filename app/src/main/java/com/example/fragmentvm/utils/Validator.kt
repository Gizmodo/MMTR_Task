package com.example.fragmentvm.utils

class Validator {
    companion object {
        @JvmStatic
        val EMAIL_REGEX = "^[A-Za-z](.*)([@])(.+)(\\.)(.+)"

        fun isEmailValid(email: String): Boolean {
            return EMAIL_REGEX.toRegex().matches(email)
        }

        fun isDescriptionValid(description: String): Boolean {
            return description.isNotEmpty()
        }
    }
}
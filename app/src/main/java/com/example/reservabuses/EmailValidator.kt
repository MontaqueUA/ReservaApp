package com.example.ReservaBuses

import java.util.regex.Pattern

class EmailValidator {
    companion object {
        fun isValidEmail(email: String): Boolean {
            val emailRegex: String = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                    "[a-zA-Z0-9_+&*-]+)*@" +
                    "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                    "A-Z]{2,7}$"

            val pat : Pattern = Pattern.compile(emailRegex)
            return pat.matcher(email).matches()
        }
    }
}
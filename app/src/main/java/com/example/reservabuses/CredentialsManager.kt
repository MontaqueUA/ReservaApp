package com.example.reservabuses

import android.content.Context
import android.content.SharedPreferences
import com.example.reservabuses.SingletonHolder
import com.example.reservabuses.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CredentialsManager private constructor(context: Context){
    private val context = context
    init {
        // Init with context if needed
    }
    companion object : SingletonHolder<CredentialsManager, Context>(::CredentialsManager)

    private fun getCredentialsSharedPreferences() : SharedPreferences {
        return context.getSharedPreferences(
            context.getString(R.string.credentials_shared_preference_file),
            Context.MODE_PRIVATE)
    }

    fun saveUser(userEmail: String, userPassword: String) {
        // Should be saved hashed :D
        val sharedPref = getCredentialsSharedPreferences()
        with (sharedPref.edit()) {
            putString(context.getString(R.string.useremail_credential_key), userEmail)
            putString(context.getString(R.string.password_credential_key), userPassword)
            commit() // apply() does it in background
        }
    }

    fun loadUser(): Pair<String, String>? {

        val sharedPref = getCredentialsSharedPreferences()
        val email = sharedPref.getString(
            context.getString(R.string.useremail_credential_key),
            context.getString(R.string.default_empty_credential_value))
        val password = sharedPref.getString(
            context.getString(R.string.password_credential_key),
            context.getString(R.string.default_empty_credential_value))
        return when {
            email.isNullOrEmpty() || password.isNullOrEmpty() -> null
            else -> Pair(email, password)
        }
    }

    fun deleteUser() {
        val sharedPref = getCredentialsSharedPreferences()
        with (sharedPref.edit()) {
            putString(
                context.getString(R.string.useremail_credential_key),
                context.getString(R.string.default_empty_credential_value))
            putString(
                context.getString(R.string.password_credential_key),
                context.getString(R.string.default_empty_credential_value))
            commit() // apply() does it in background
        }
    }
}
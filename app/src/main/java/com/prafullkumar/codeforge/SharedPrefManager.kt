package com.prafullkumar.codeforge

import android.content.Context

class SharedPrefManager(
    context: Context
) {

    companion object {
        const val PREF_NAME = "codeforge"
        const val FIRST_TIME_COMPLETED = "first_time_completed"
    }

    private val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun setFirstTimeCompleted() {
        pref.edit().putBoolean(FIRST_TIME_COMPLETED, true).apply()
    }

    fun isFirstTimeCompleted() = pref.getBoolean(FIRST_TIME_COMPLETED, false)
}
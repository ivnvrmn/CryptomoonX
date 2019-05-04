package com.rmnivnv.cryptomoonx.model

import android.content.Context

private const val PREFERENCES_NAME = "com.rmnivnv.cryptomoonx"
private const val KEY_TOP_COINS_UPDATED_TIME = "topCoinsUpdatedTime"

class PreferencesImpl(context: Context) : Preferences {
    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    override var topCoinsUpdatedTime: Long
        get() = preferences.getLong(KEY_TOP_COINS_UPDATED_TIME, -1)
        set(value) = preferences.edit().putLong(KEY_TOP_COINS_UPDATED_TIME, value).apply()
}
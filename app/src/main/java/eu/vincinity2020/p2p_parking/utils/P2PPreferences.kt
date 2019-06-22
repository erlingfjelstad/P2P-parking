package eu.vincinity2020.p2p_parking.utils

import android.content.Context
import android.content.SharedPreferences
import eu.vincinity2020.p2p_parking.app.common.AppConstants

open class P2PPreferences(context: Context) {

    private val PREFERENCES = AppConstants.SHARED_PREFS

    private var mSharedPreferences: SharedPreferences

    init {
        mSharedPreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
    }

    fun saveString(key: String, value: String) {
        val editor = mSharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }
    fun saveInt(key: String, value: Int) {
        val editor = mSharedPreferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun saveLong(key: String, value: Long) {
        val editor = mSharedPreferences.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    fun saveBoolean(key: String, value: Boolean) {
        val editor = mSharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun saveFloat(key: String, value: Float) {
        val editor = mSharedPreferences.edit()
        editor.putFloat(key, value)
        editor.apply()
    }

    fun clear() {
        val editor = mSharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    fun getString(key: String): String? {
        return mSharedPreferences.getString(key, null)
    }


    fun getLong(key: String): Long {
        return mSharedPreferences.getLong(key, 0L)
    }


    fun getInt(key: String): Int {
        return mSharedPreferences.getInt(key, 0)
    }

    fun getFloat(key: String): Double {
        return mSharedPreferences.getFloat(key, 0.0f).toDouble()
    }


    fun getBoolean(key: String, defValue: Boolean = false): Boolean {
        return mSharedPreferences.getBoolean(key, defValue)
    }
}
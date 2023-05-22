package com.test.archi.utils

import android.util.Log
import com.test.archi.BuildConfig
import javax.inject.Inject

class DLog @Inject constructor() {
    private val LOG = BuildConfig.DEBUG

    fun i(tag: String?, string: String?) {
        try {
            if (LOG) Log.i(tag, string!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun e(tag: String?, string: String?) {
        try {
            if (LOG) Log.e(tag, string!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun e(tag: String?, string: String?, tr: Throwable?) {
        try {
            if (LOG) Log.e(tag, string, tr)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun d(tag: String?, string: String?) {
        try {
            if (LOG) Log.d(tag, string!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun v(tag: String?, string: String?) {
        try {
            if (LOG) Log.v(tag, string!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun w(tag: String?, string: String?) {
        try {
            if (LOG) Log.w(tag, string!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
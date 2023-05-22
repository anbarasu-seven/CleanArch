package com.test.archi.utils

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

private fun Context.printKeyHash() {
    // Add code to print out the key hash
    try {
        val info: PackageInfo =
            packageManager.getPackageInfo("com.tstl.dukan", PackageManager.GET_SIGNATURES)
        for (signature in info.signatures) {
            val md = MessageDigest.getInstance("SHA")
            md.update(signature.toByteArray())
            //dLog.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            //dLog.e("keyHash1:", Base64.encodeToString(md.digest(), Base64.NO_WRAP))
        }
    } catch (e: PackageManager.NameNotFoundException) {
        //dLog.e("KeyHash:", e.toString())
    } catch (e: NoSuchAlgorithmException) {
        //dLog.e("KeyHash:", e.toString())
    }
}
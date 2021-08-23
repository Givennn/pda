package com.panda.pda.app.user

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.panda.pda.app.user.data.model.LoginDataModel
import timber.log.Timber

/**
 * created by AnJiwei 2021/8/17
 */
class UserViewModel: ViewModel() {
    val loginData by lazy { MutableLiveData<LoginDataModel>() }

    fun getAppVersionName(context: Context): String {
        var versionName = ""
        try {
            // ---get the package info---
            val pm = context.packageManager
            val pi = pm.getPackageInfo(context.packageName, 0)
            versionName = pi.versionName
            //            versioncode = pi.versionCode;
            if (versionName == null || versionName.isEmpty()) {
                return ""
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
        return "V${versionName}"
    }
}
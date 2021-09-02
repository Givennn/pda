package com.panda.pda.app.user

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.panda.pda.app.base.retrofit.QueryParamInterceptor
import com.panda.pda.app.user.data.model.LoginDataModel
import com.panda.pda.app.user.data.model.LoginRequest
import timber.log.Timber

/**
 * created by AnJiwei 2021/8/17
 */
class UserViewModel(val app: Application): AndroidViewModel(app) {

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

    fun updateLoginData(dataModel: LoginDataModel, request: LoginRequest) {
        loginData.postValue(dataModel)
        QueryParamInterceptor.TOKEN = dataModel.token
        app.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
            .edit {
                putString(SP_USER_NAME, request.workCode)
                putString(SP_PASSWORD, request.password)
            }
    }

    companion object {
        val SP_NAME = UserViewModel::class.simpleName
        const val SP_USER_NAME = "UserName"
        const val SP_PASSWORD = "password"
    }
}
package com.panda.pda.app.user

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.panda.pda.app.PdaApplication
import com.panda.pda.app.base.retrofit.QueryParamInterceptor
import com.panda.pda.app.user.data.model.LoginDataModel
import com.panda.pda.app.user.data.model.LoginRequest
import timber.log.Timber

/**
 * created by AnJiwei 2021/8/17
 */
class UserViewModel(app: Application) : AndroidViewModel(app) {

    val loginData by lazy { MutableLiveData<LoginDataModel>() }
    val logoutActionData by lazy { MutableLiveData<String?>() }

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
        getApplication<PdaApplication>().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
            .edit {
                putString(SP_USER_NAME, request.workCode)
                putString(SP_PASSWORD, request.password)
            }
    }

    fun logout(reasonCode: LogoutReasonCode) {
        val app = getApplication<PdaApplication>()
        app.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
            .edit {
                putString(SP_USER_NAME, null)
                putString(SP_PASSWORD, null)
            }
        logoutActionData.postValue(reasonCode.message)
    }


    companion object {
        val SP_NAME = UserViewModel::class.simpleName
        const val SP_USER_NAME = "UserName"
        const val SP_PASSWORD = "password"
        val IgnoreToastCodeList = listOf<Int>()
    }
}

enum class LogoutReasonCode(val code: Int, val message: String) {
    PwdChanged(40001, ""),
    LoginError(41101, "");

    companion object {
        fun findReason(code: Int): LogoutReasonCode? {
            Timber.e(code.toString())
            return values().firstOrNull { it.code == code }
        }
    }
}

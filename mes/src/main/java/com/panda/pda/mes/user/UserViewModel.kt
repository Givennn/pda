package com.panda.pda.mes.user

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.panda.pda.mes.PdaApplication
import com.panda.pda.mes.base.retrofit.QueryParamInterceptor
import com.panda.pda.mes.user.data.model.LoginDataModel
import com.panda.pda.mes.user.data.model.LoginRequest
import timber.log.Timber

/**
 * created by AnJiwei 2021/8/17
 */
class UserViewModel(app: Application) : AndroidViewModel(app) {

    val loginData by lazy { MutableLiveData<LoginDataModel>() }
    val logoutActionData by lazy { MutableLiveData<Int?>() }

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

    fun updateLoginData(dataModel: LoginDataModel, request: LoginRequest?, qrcode: String?) {
        loginData.postValue(dataModel)
        QueryParamInterceptor.TOKEN = dataModel.token
        getApplication<PdaApplication>().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
            .edit {
                if (request != null) {
                    putString(SP_USER_NAME, request.workCode)
                    putString(SP_PASSWORD, request.password)
                    putString(SP_LOGIN_TYPE, LoginMode.PWD.spKey)
                }
                if (qrcode != null) {
                    putString(SP_QR_CODE, qrcode)
                    putString(SP_LOGIN_TYPE, LoginMode.QRCODE.spKey)
                }

            }
    }

    fun logout(reasonCode: Int? = null) {
        val app = getApplication<PdaApplication>()
        app.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
            .edit {
                putString(SP_USER_NAME, null)
                putString(SP_PASSWORD, null)
            }

        logoutActionData.postValue(reasonCode)
    }


    companion object {
        val SP_NAME = UserViewModel::class.simpleName
        const val SP_USER_NAME = "UserName"
        const val SP_PASSWORD = "password"
        const val SP_LOGIN_TYPE = "loginType"
        const val SP_QR_CODE = "qrcode"
        val IgnoreToastCodeList = listOf(
            41205, // 角色不存在
            41211, // 没有权限,无法登录
        )

        val LogoutCodeList = listOf(
            41101,
            50014, //LoseEfficacy
            50006
        )
    }
}

enum class LoginMode(val spKey: String) {

    PWD("pwd"),
    QRCODE("code")
}

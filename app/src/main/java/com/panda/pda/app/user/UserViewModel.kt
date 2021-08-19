package com.panda.pda.app.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.panda.pda.app.user.data.model.LoginDataModel

/**
 * created by AnJiwei 2021/8/17
 */
class UserViewModel: ViewModel() {
    val loginData by lazy { MutableLiveData<LoginDataModel>() }
}
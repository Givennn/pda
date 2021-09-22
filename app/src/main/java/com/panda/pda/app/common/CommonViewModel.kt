package com.panda.pda.app.common

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.panda.pda.app.R
import com.panda.pda.app.common.data.model.AuthorityModel

/**
 * created by AnJiwei 2021/9/14
 */
class CommonViewModel : ViewModel() {

    val authorityViewModel = MutableLiveData<List<AuthorityModel>>()

}
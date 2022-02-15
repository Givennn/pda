package com.panda.pda.mes.common

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.panda.pda.mes.common.data.model.AuthorityModel

/**
 * created by AnJiwei 2021/9/14
 */
class CommonViewModel : ViewModel() {

    val authorityViewModel = MutableLiveData<List<AuthorityModel>>()

}
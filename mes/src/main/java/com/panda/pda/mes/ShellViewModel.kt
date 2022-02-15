package com.panda.pda.mes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

/**
 * created by AnJiwei 2021/8/19
 */
class ShellViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    val bottomNavIdData = MutableLiveData<Int>()

    fun setQuery(query: String) {
        savedStateHandle["query"] = query
    }
}
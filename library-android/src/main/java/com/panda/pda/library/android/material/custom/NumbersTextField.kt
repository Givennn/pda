package com.panda.pda.library.android.material.custom

import android.content.Context
import com.google.android.material.textfield.TextInputLayout

/**
 * created by AnJiwei 2021/9/8
 */
class NumbersTextField : TextInputLayout {

    constructor(context: Context) : super(context) {

    }

    private var minValue = 0
    private val maxValue = Int.MAX_VALUE


}
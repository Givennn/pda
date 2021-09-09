package com.panda.pda.library.android.material.custom

import android.content.Context
import android.content.res.ColorStateList
import android.text.InputFilter
import android.text.Spanned
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputLayout
import com.panda.pda.library.android.R


/**
 * created by AnJiwei 2021/9/8
 */
class NumbersTextField(context: Context, attrs: AttributeSet?) : TextInputLayout(context, attrs) {

    private var minValue = 0
    var maxValue = 10
        set(value) {
            field = value
            post {
                updateIconStatus(value)
                updateFilter(value)
            }

        }

    private fun updateFilter(value: Int) {
        editText?.filters = arrayOf(
            InputFilterMinMax(minValue, value)
        )
    }

    val getValue: Int
        get() {
            return count
        }
    private var count = 0


    @ColorInt
    private var iconDisableColor: Int? = null

    @ColorInt
    private var iconEnableColor: Int? = null

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.numbersTextField,
            0, 0).apply {
            try {
                if (hasValue(R.styleable.numbersTextField_iconEnableColor)) {
                    iconEnableColor = getColor(R.styleable.numbersTextField_iconEnableColor,
                        context.getColor(R.color.design_default_color_primary))
                }
                if (hasValue(R.styleable.numbersTextField_iconDisableColor)) {
                    iconDisableColor = getColor(R.styleable.numbersTextField_iconDisableColor,
                        context.getColor(R.color.design_default_color_primary))
                }
                if (hasValue(R.styleable.numbersTextField_maxNumber)) {
                    maxValue = getInteger(R.styleable.numbersTextField_maxNumber, Int.MAX_VALUE)
                }
            } finally {
                recycle()
            }

        }
        setEndIconOnClickListener {
            updateEditText(++count)
            updateIconStatus(count)
        }
        setStartIconOnClickListener {
            updateEditText(--count)
            updateIconStatus(count)
        }
        post {
            updateIconStatus(count)
        }
        editText?.doAfterTextChanged { text ->
            try {
                val number = text.toString().toInt()
                count = number
                updateIconStatus(number)
            } finally {
            }
        }
    }

    private fun updateIconStatus(number: Int) {
        updateStartIconStatus(number > minValue)
        updateEndIconStatus(number < maxValue)
    }

    private fun updateEditText(number: Int) {
        count = when {
            number < 0 -> 0
            number > maxValue -> maxValue
            else -> number
        }
        editText?.setText(count.toString())
    }

    private fun updateStartIconStatus(enable: Boolean) {
        if (enable) {
            if (iconEnableColor != null) {
                setStartIconTintList(ColorStateList.valueOf(iconEnableColor!!))
            }
        } else {
            if (iconDisableColor != null) {
                setStartIconTintList(ColorStateList.valueOf(iconDisableColor!!))
            }
        }
    }

    private fun updateEndIconStatus(enable: Boolean) {
        if (enable) {
            if (iconEnableColor != null) {
                setEndIconTintList(ColorStateList.valueOf(iconEnableColor!!))
            }
        } else {
            if (iconDisableColor != null) {
                setEndIconTintList(ColorStateList.valueOf(iconDisableColor!!))
            }
        }
    }
}

class InputFilterMinMax : InputFilter {
    private var min: Int
    private var max: Int

    constructor(min: Int, max: Int) {
        this.min = min
        this.max = max
    }

    constructor(min: String, max: String) {
        this.min = min.toInt()
        this.max = max.toInt()
    }

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int,
    ): CharSequence? {
        try {
            val input = (dest.toString() + source.toString()).toInt()
            if (isInRange(min, max, input)) return null
        } catch (nfe: NumberFormatException) {
        }
        return ""
    }

    private fun isInRange(a: Int, b: Int, c: Int): Boolean {
        return if (b > a) c in a..b else c in b..a
    }
}
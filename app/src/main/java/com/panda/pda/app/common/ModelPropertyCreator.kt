package com.panda.pda.app.common

import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.panda.pda.app.R
import com.panda.pda.app.common.data.CommonParameters
import com.panda.pda.app.common.data.DataParamType

/**
 * created by AnJiwei 2021/9/27
 */
class ModelPropertyCreator<TSource>(
    private val modelClass: Class<TSource>,
    linearLayout: LinearLayout,
    startIndex: Int = 0,
    tag: String = ""
) {

    private val propertyMap = mutableMapOf<String, TextView>()

    private val sourceFields by lazy {
        modelClass.declaredFields.filter {
            if (it.isAnnotationPresent(
                    ModelProperty::class.java
                )
            ) {
                val modelProperty = it.getAnnotation(ModelProperty::class.java)!!
                modelProperty.tag.isEmpty() || modelProperty.tag.contains(tag)
            } else {
                false
            }
        }
    }

    init {
        sourceFields.map { it.getAnnotation(ModelProperty::class.java)!! }
            .sortedBy { it.order }
            .reversed()
            .forEach {
                val label = it.labelName
                val propertyLayout =
                    LayoutInflater.from(linearLayout.context)
                        .inflate(R.layout.frame_detail_model_property, null)
                val tvLabel = propertyLayout.findViewById<TextView>(R.id.tv_label)
                val tvValue = propertyLayout.findViewById<TextView>(R.id.tv_value)
                tvLabel.text = label
                linearLayout.addView(propertyLayout, startIndex)
                propertyMap[label] = tvValue
            }
    }

    private fun getDataParameter(key: Int, type: String): String {
        return CommonParameters.getDesc(DataParamType.valueOf(type), key)
    }

    fun setData(data: TSource) {
        sourceFields
            .forEach {
                it.isAccessible = true
                val modelProperty = it.getAnnotation(ModelProperty::class.java)!!
                val value = if (modelProperty.dataParameterType.isEmpty()) {
                    it.get(data)?.toString()
                } else {
                    getDataParameter(it.getInt(data), modelProperty.dataParameterType)
                }
                propertyMap[modelProperty.labelName]?.text = value
            }
    }

}

@Target(AnnotationTarget.FIELD)
annotation class ModelProperty(
    val order: Int,
    val labelName: String,
    val tag: Array<String> = [],
    val dataParameterType: String = ""
)

data class Test(
    @ModelProperty(1, "test")
    val id: Int
)
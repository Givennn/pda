package com.panda.pda.mes.base.extension

import android.os.Bundle
import androidx.core.os.bundleOf
import com.squareup.moshi.Moshi
import java.lang.reflect.ParameterizedType

fun Bundle.putAny(key: String, value: Any?) {
    putAll(bundleOf(key to value))
}

inline fun <reified T> Bundle.putObjectString(value: T, key: String? = null) {
    val clazz = T::class.java
    val jsonAdapter = Moshi.Builder().build().adapter(clazz)
    this.putString(key ?: clazz.simpleName, jsonAdapter.toJson(value))
}

inline fun <reified T>  Bundle.putGenericObjectString(value: T, types: ParameterizedType, key: String? = null) {
    val jsonAdapter = Moshi.Builder().build().adapter<T>(types)
    this.putString(key ?: types.toString(), jsonAdapter.toJson(value))
}

inline fun <reified T> Bundle.getStringObject(key: String? = null): T? {
    val clazz = T::class.java
    val jsonAdapter = Moshi.Builder().build().adapter(clazz)
    val json = this.getString(key ?: clazz.simpleName)
    return jsonAdapter.fromJson(json ?: return null)
}

inline fun <reified T>  Bundle.getGenericObjectString(types: ParameterizedType, key: String? = null): T? {
    val jsonAdapter = Moshi.Builder().build().adapter<T>(types)
    val json = this.getString(key ?: types.toString())
    return jsonAdapter.fromJson(json ?: return null)
}



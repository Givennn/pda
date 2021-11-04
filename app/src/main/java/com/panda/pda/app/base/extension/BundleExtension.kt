package com.panda.pda.app.base.extension

import android.os.Bundle
import androidx.core.os.bundleOf
import com.squareup.moshi.Moshi

fun Bundle.putAny(key: String, value: Any?) {
    putAll(bundleOf(key to value))
}

inline fun <reified T> Bundle.putObjectString(value: T) {
    val clazz = T::class.java
    val jsonAdapter = Moshi.Builder().build().adapter(clazz)
    this.putString(clazz.simpleName, jsonAdapter.toJson(value))
}

inline fun <reified T> Bundle.getStringObject(key: String): T? {
    val clazz = T::class.java
    val jsonAdapter = Moshi.Builder().build().adapter(clazz)
    val json = this.getString(clazz.simpleName)
    return jsonAdapter.fromJson(json ?: return null)
}

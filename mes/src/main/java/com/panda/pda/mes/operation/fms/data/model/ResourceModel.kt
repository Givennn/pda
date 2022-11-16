package com.panda.pda.mes.operation.fms.data.model

/**
 * created by AnJiwei 2022/11/15
 */
data class ResourceModel(
    val id: Int,
    val originalId: Int,
    val resource: String,
    val resourceCode: String,
    val resourceType: String,
) {
    val resourceDisplayName: String get() = "$resourceCode  $resource"
}


enum class ResourceType(val code: Int) {
    Person(0),
    Group(1),
    Equipment(2),
    Undefined(3)
}
package com.panda.pda.mes.common.data.model

import com.panda.pda.library.android.controls.ContactsIndex.ContactsUtils

/**
 * created by AnJiwei 2022/7/20
 */
data class PersonModel(
    val id: Int,
    val orgId: Int,
    val roleNameStrList: List<String>,
    val status: Int,
    val updateId: Int,
    val updateName: String,
    val updateTime: String,
    val userName: String,
    val workCode: String,
    var isSelected: Boolean = false,
) : Comparable<PersonModel> {
    //    @Transient
//    val abbreviation by lazy { ContactsUtils.getAbbreviation(userName) }
//
//    @Transient
//    var initial = abbreviation.substring(0, 1)
    var abbreviation: String
    var initial: String
    init {
        abbreviation = ContactsUtils.getAbbreviation(userName)
        initial = abbreviation.substring(0, 1)
    }
    override fun compareTo(other: PersonModel): Int {
//        if (abbreviation == null) {
//            abbreviation = ContactsUtils.getAbbreviation(userName)
//            initial = abbreviation!!.substring(0, 1)
//        }
//        if (other.abbreviation == null) {
//            other.abbreviation = ContactsUtils.getAbbreviation(other.userName)
//            other.initial = other.abbreviation!!.substring(0, 1)
//        }
        if (abbreviation == other.abbreviation) {
            return 0
        }
        var flag: Boolean
        return if (abbreviation.startsWith("#")
                .also { flag = it } xor other.abbreviation.startsWith("#")
        ) {
            if (flag) 1 else -1
        } else initial.compareTo(other.initial)
    }
}
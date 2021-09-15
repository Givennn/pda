package com.panda.pda.app.common.data.model

/**
 * created by AnJiwei 2021/9/14
 */
data class AuthorityModel(
    val category: Int,
    val children: List<AuthorityModel>,
    val id: Int,
    val name: String,
    val parentId: Int,
    val path: String,
    val type: String,
//    val props: Props
)

//data class Props(
//    val receive: Receive
//)
//
//data class Receive(
//    val category: Int,
//    val id: Int,
//    val name: String
//)
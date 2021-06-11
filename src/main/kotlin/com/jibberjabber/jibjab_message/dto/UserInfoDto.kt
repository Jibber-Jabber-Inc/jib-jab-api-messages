package com.jibberjabber.jibjab_message.dto

data class UserInfoDto(
    var id: String,
    var username: String,
    var email: String,
    var firstName: String,
    var lastName: String,
    var role: String?
)

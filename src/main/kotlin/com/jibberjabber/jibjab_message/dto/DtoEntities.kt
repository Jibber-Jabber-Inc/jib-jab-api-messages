package com.jibberjabber.jibjab_message.dto

data class UserInfoDto(
    var id: String,
    var username: String,
    var email: String,
    var firstName: String,
    var lastName: String,
    var role: String?
)


data class CreationChatMessageDto(
    var senderId: String,
    var recipientId: String,
    var content: String,
)

data class ChatMessageReadDto(
    var messageId: String
)
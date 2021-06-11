package com.jibberjabber.jibjab_message.domain

import java.time.LocalDateTime

class ChatMessage(
    var type: MessageType? = null,
    var content: String? = null,
    var sender: String? = null,
    var receiver: String? = null,
    var dateTime: LocalDateTime = LocalDateTime.now()
)

enum class MessageType {
    CHAT, JOIN, LEAVE, TYPING
}
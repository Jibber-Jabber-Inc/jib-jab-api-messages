package com.jibberjabber.jibjab_message.domain

import java.util.*
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "chat_room")
class ChatRoom(
    var chatId: String? = null,
    var senderId: String? = null,
    var recipientId: String? = null
) : AbstractEntity()

@Entity
@Table(name = "chat_message")
class ChatMessage(
    var chatId: String? = null,
    var senderId: String? = null,
    var recipientId: String? = null,
    var senderName: String? = null,
    var recipientName: String? = null,
    var content: String? = null,
    var timestamp: Date? = null,
    var status: MessageStatus? = null
) : AbstractEntity()

//@Entity
//@Table(name = "chat_notification")
//class ChatNotification(
//    var senderId: String? = null,
//    var senderName: String? = null
//) : AbstractEntity()

enum class MessageStatus {
    RECEIVED, DELIVERED
}
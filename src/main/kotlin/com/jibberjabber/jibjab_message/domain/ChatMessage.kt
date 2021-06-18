package com.jibberjabber.jibjab_message.domain

import org.hibernate.annotations.CreationTimestamp
import java.sql.Timestamp
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
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
    var senderId: String,
    var recipientId: String,
    var content: String,
    @CreationTimestamp
    var timestamp: Timestamp? = null,
    @Enumerated(value = EnumType.STRING)
    var status: MessageStatus? = null
) : AbstractEntity()


enum class MessageStatus {
    RECEIVED, READ
}
package com.jibberjabber.jibjab_message.service

import com.jibberjabber.jibjab_message.domain.ChatMessage
import com.jibberjabber.jibjab_message.domain.MessageStatus
import com.jibberjabber.jibjab_message.dto.CreationChatMessageDto
import com.jibberjabber.jibjab_message.factory.ChatMessageFactory
import com.jibberjabber.jibjab_message.repository.ChatMessageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ChatMessageService @Autowired constructor(
    val chatMessageRepository: ChatMessageRepository,
    val chatRoomService: ChatRoomService,
    val chatMessageFactory: ChatMessageFactory
) {

    fun save(messageDto: CreationChatMessageDto, chatId: String?): ChatMessage {
        val chatMessage = chatMessageFactory.from(messageDto, chatId!!, MessageStatus.RECEIVED)
        return chatMessageRepository.save(chatMessage)
    }

    fun getChatMessage(messageId: String): ChatMessage {
        return chatMessageRepository.getOne(messageId)
    }

    fun countNewMessages(senderId: String, recipientId: String): Long {
        return chatMessageRepository.countBySenderIdAndRecipientIdAndStatus(
            senderId, recipientId, MessageStatus.RECEIVED
        )
    }

    fun findChatMessages(senderId: String, recipientId: String): List<ChatMessage> {
        val chatId: String? = chatRoomService.getChatId(senderId, recipientId, false)
        val messages = if (chatId != null) chatMessageRepository.findByChatId(chatId) else listOf()
        if (messages.isNotEmpty())
            chatMessageRepository.updateStatuses(senderId, recipientId, MessageStatus.READ)
        return messages
    }

    fun markMessageAsRead(messageId: String) {
        chatMessageRepository.updateStatus(messageId, MessageStatus.READ)
    }

}

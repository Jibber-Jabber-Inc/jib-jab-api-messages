package com.jibberjabber.jibjab_message.service

import com.jibberjabber.jibjab_message.domain.ChatMessage
import com.jibberjabber.jibjab_message.domain.MessageStatus
import com.jibberjabber.jibjab_message.exception.ResourceNotFoundException
import com.jibberjabber.jibjab_message.repository.ChatMessageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ChatMessageService @Autowired constructor(
    val chatMessageRepository: ChatMessageRepository,
    val chatRoomService: ChatRoomService
) {

    fun save(chatMessage: ChatMessage): ChatMessage {
        chatMessage.status = MessageStatus.RECEIVED
        chatMessageRepository.save(chatMessage)
        return chatMessage
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
            chatMessageRepository.updateStatuses(senderId, recipientId, MessageStatus.DELIVERED)
        return messages
    }

    fun findById(id: String): ChatMessage {
        return chatMessageRepository
            .findById(id)
            .map { chatMessage ->
                chatMessage.status = MessageStatus.DELIVERED
                chatMessageRepository.save(chatMessage)
            }
            .orElseThrow { ResourceNotFoundException("can't find message ($id)") }
    }

}

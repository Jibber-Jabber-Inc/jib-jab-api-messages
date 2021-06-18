package com.jibberjabber.jibjab_message.service

import com.jibberjabber.jibjab_message.domain.ChatRoom
import com.jibberjabber.jibjab_message.repository.ChatRoomRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ChatRoomService @Autowired constructor(
    val chatRoomRepository: ChatRoomRepository,
) {
    fun getChatId(senderId: String, recipientId: String, createIfNotExist: Boolean): String? {
        return chatRoomRepository.findBySenderIdAndRecipientId(senderId, recipientId).map(ChatRoom::chatId).orElseGet {
            when (createIfNotExist) {
                true -> {
                    val chatId = String.format("%s_%s", senderId, recipientId)
                    chatRoomRepository.save(ChatRoom(chatId, senderId, recipientId)) //senderRecipient
                    chatRoomRepository.save(ChatRoom(chatId, recipientId, senderId)) //recipientSender
                    chatId
                }
                else -> null
            }
        }
    }
}

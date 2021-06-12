package com.jibberjabber.jibjab_message.factory

import com.jibberjabber.jibjab_message.domain.ChatMessage
import com.jibberjabber.jibjab_message.domain.MessageStatus
import com.jibberjabber.jibjab_message.dto.CreationChatMessageDto
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class ChatMessageFactory : AbstractFactory<ChatMessage, CreationChatMessageDto> {

    override fun convert(input: ChatMessage): CreationChatMessageDto {
        return CreationChatMessageDto(input.senderId, input.recipientId, input.content)
    }

    fun from(input: CreationChatMessageDto, chatId: String, status: MessageStatus): ChatMessage {
        return ChatMessage(chatId, input.senderId, input.recipientId, input.content, LocalDateTime.now(), status)
    }

}
package com.jibberjabber.jibjab_message.controller

import com.jibberjabber.jibjab_message.domain.ChatMessage
import com.jibberjabber.jibjab_message.dto.ChatMessageReadDto
import com.jibberjabber.jibjab_message.factory.ChatMessageFactory
import com.jibberjabber.jibjab_message.service.ChatMessageService
import com.jibberjabber.jibjab_message.service.ChatRoomService
import com.jibberjabber.jibjab_message.utils.SessionUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable


@Controller
class ChatController @Autowired constructor(
    val messagingTemplate: SimpMessagingTemplate,
    val chatMessageService: ChatMessageService,
    val chatRoomService: ChatRoomService,
    val sessionUtils: SessionUtils,
    val chatMessageFactory: ChatMessageFactory
) {

    @MessageMapping("/chat")
    fun processMessage(@Payload chatMessage: ChatMessage) {
        val chatId = chatRoomService.getChatId(chatMessage.senderId, chatMessage.recipientId, true)
        val saved: ChatMessage = chatMessageService.save(chatMessageFactory.convert(chatMessage), chatId)
        messagingTemplate.convertAndSendToUser(chatMessage.recipientId, "/queue/messages", saved)
        messagingTemplate.convertAndSendToUser(chatMessage.senderId, "/queue/messages", saved)
    }

    @MessageMapping("/read")
    fun processReadChat(@Payload chatMessageRead: ChatMessageReadDto) {
        val chatMessage = chatMessageService.getChatMessage(chatMessageRead.messageId)
        print("chatMessage $chatMessage")
        chatMessageService.markMessageAsRead(chatMessage.id!!)
        val saved = chatMessageService.getChatMessage(chatMessageRead.messageId)
        messagingTemplate.convertAndSendToUser(chatMessage.recipientId, "/queue/read", saved)
        messagingTemplate.convertAndSendToUser(chatMessage.senderId, "/queue/read", saved)
    }

    @GetMapping("/api/message/messages/{userId}/count")
    fun countNewMessages(@PathVariable userId: String): ResponseEntity<Long> {
        val user = sessionUtils.getTokenUserInformation()
        return ResponseEntity.ok(chatMessageService.countNewMessages(userId, user.id))
    }

    @GetMapping("/api/message/messages/{userId}")
    fun findChatMessages(@PathVariable userId: String): ResponseEntity<*> {
        val user = sessionUtils.getTokenUserInformation()
        return ResponseEntity.ok(chatMessageService.findChatMessages(userId, user.id).sortedBy { it.timestamp })
    }

//    @GetMapping("/messages/{id}")
//    fun findMessage(@PathVariable id: String): ResponseEntity<*> {
//        return ResponseEntity.ok(chatMessageService.findById(id))
//    }

}

package com.jibberjabber.jibjab_message.controller

import com.jibberjabber.jibjab_message.domain.ChatMessage
import com.jibberjabber.jibjab_message.service.ChatMessageService
import com.jibberjabber.jibjab_message.service.ChatRoomService
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
    val chatRoomService: ChatRoomService
) {

    @MessageMapping("/chat")
    fun processMessage(@Payload chatMessage: ChatMessage) {
        val chatId = chatRoomService.getChatId(chatMessage.senderId!!, chatMessage.recipientId!!, true)
        chatMessage.chatId = chatId
        val saved: ChatMessage = chatMessageService.save(chatMessage)
        messagingTemplate.convertAndSendToUser(chatMessage.recipientId!!, "/queue/messages", saved)
    }

    @GetMapping("/messages/{senderId}/{recipientId}/count")
    fun countNewMessages(@PathVariable senderId: String, @PathVariable recipientId: String): ResponseEntity<Long> {
        return ResponseEntity.ok(chatMessageService.countNewMessages(senderId, recipientId))
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    fun findChatMessages(@PathVariable senderId: String, @PathVariable recipientId: String): ResponseEntity<*> {
        return ResponseEntity.ok(chatMessageService.findChatMessages(senderId, recipientId))
    }

    @GetMapping("/messages/{id}")
    fun findMessage(@PathVariable id: String): ResponseEntity<*> {
        return ResponseEntity.ok(chatMessageService.findById(id))
    }

}

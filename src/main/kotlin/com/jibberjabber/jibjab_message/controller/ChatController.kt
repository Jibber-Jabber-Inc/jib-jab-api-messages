package com.jibberjabber.jibjab_message.controller

import com.jibberjabber.jibjab_message.domain.ChatMessage
import com.jibberjabber.jibjab_message.dto.CreationChatMessageDto
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
    val sessionUtils: SessionUtils
) {

    @MessageMapping("/chat")
    fun processMessage(@Payload messageDto: CreationChatMessageDto) {
        val chatId = chatRoomService.getChatId(messageDto.senderId, messageDto.recipientId, true)
        val saved: ChatMessage = chatMessageService.save(messageDto, chatId)
        messagingTemplate.convertAndSendToUser(messageDto.recipientId, "/queue/messages", saved)
        messagingTemplate.convertAndSendToUser(messageDto.senderId, "/queue/messages", saved)
    }

    @GetMapping("/messages/{userId}/count")
    fun countNewMessages(@PathVariable userId: String): ResponseEntity<Long> {
        val user = sessionUtils.getTokenUserInformation()
        return ResponseEntity.ok(chatMessageService.countNewMessages(userId, user.id))
    }

    @GetMapping("/messages/{userId}")
    fun findChatMessages(@PathVariable userId: String): ResponseEntity<*> {
        val user = sessionUtils.getTokenUserInformation()
        return ResponseEntity.ok(chatMessageService.findChatMessages(userId, user.id))
    }

//    @GetMapping("/messages/{id}")
//    fun findMessage(@PathVariable id: String): ResponseEntity<*> {
//        return ResponseEntity.ok(chatMessageService.findById(id))
//    }

}

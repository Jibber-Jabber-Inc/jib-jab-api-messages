package com.jibberjabber.jibjab_message.controller

import com.jibberjabber.jibjab_message.domain.ChatMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller


@Controller
class ChatController @Autowired constructor(
    private val simpMessagingTemplate: SimpMessagingTemplate
) {
    /*-------------------- Group (Public) chat--------------------*/
    @MessageMapping("/sendMessage")
    @SendTo("/topic/pubic")
    fun sendMessage(@Payload chatMessage: ChatMessage): ChatMessage {
        return chatMessage
    }

    @MessageMapping("/addUser")
    @SendTo("/topic/pubic")
    fun addUser(
        @Payload chatMessage: ChatMessage,
        headerAccessor: SimpMessageHeaderAccessor
    ): ChatMessage {
        // Add user in web socket session
        headerAccessor.sessionAttributes!!["username"] = chatMessage.sender
        return chatMessage
    }

    /*--------------------Private chat--------------------*/

    @MessageMapping("/sendPrivateMessage") //@SendTo("/queue/reply")
    fun sendPrivateMessage(@Payload chatMessage: ChatMessage) {
        simpMessagingTemplate.convertAndSendToUser(
            chatMessage.receiver!!.trim(), "/reply", chatMessage
        )
//        return chatMessage;
    }

    @MessageMapping("/addPrivateUser")
    @SendTo("/queue/reply")
    fun addPrivateUser(
        @Payload chatMessage: ChatMessage,
        headerAccessor: SimpMessageHeaderAccessor
    ): ChatMessage {
        // Add user in web socket session
        headerAccessor.sessionAttributes!!["private-username"] = chatMessage.sender
        return chatMessage
    }
}
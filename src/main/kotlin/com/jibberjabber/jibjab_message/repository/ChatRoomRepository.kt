package com.jibberjabber.jibjab_message.repository

import com.jibberjabber.jibjab_message.domain.ChatRoom
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ChatRoomRepository : JpaRepository<ChatRoom, String> {
    fun findBySenderIdAndRecipientId(senderId: String, recipientId: String): Optional<ChatRoom>
}

package com.jibberjabber.jibjab_message.repository

import com.jibberjabber.jibjab_message.domain.ChatMessage
import com.jibberjabber.jibjab_message.domain.MessageStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import javax.transaction.Transactional

interface ChatMessageRepository : JpaRepository<ChatMessage, String> {
    fun countBySenderIdAndRecipientIdAndStatus(senderId: String, recipientId: String, status: MessageStatus): Long
    fun findByChatId(chatId: String): List<ChatMessage>

    @Query("update ChatMessage set status = ?3 where senderId = ?1 and recipientId = ?2")
    @Modifying
    @Transactional
    fun updateStatuses(senderId: String, recipientId: String, delivered: MessageStatus)

    @Query("update ChatMessage set status = ?2 where id = ?1")
    @Modifying
    @Transactional
    fun updateStatus(senderId: String, delivered: MessageStatus)
}
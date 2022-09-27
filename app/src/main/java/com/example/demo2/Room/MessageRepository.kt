package com.example.demo2.Room

import com.example.demo2.Message

class MessageRepository(
    val db : MessageDB
) {
    suspend fun upsert(message: Message) = db.getMessageDao().upsert(message)

    fun getAllText() = db.getMessageDao().getAllMessages()

}
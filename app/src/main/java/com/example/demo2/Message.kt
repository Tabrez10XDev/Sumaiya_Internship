package com.example.demo2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "Messages"
)
data class Message(
    @PrimaryKey(autoGenerate = true)
    var id : Int ?= null,
    val room: String,
    val from: String,
    val text: String,
    val time: String
)

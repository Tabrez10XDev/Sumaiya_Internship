package com.example.demo2.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.demo2.Message
import com.example.demo2.MessageDao

@Database(
    entities = [Message::class],
    version = 1
)

abstract class MessageDB : RoomDatabase() {

    abstract fun getMessageDao() : MessageDao

    companion object{
        @Volatile
        private var instance: MessageDB?= null

        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: createDB(context).also{
                instance =it
            }
        }

        private fun createDB(context: Context)=
            Room.databaseBuilder(
                context.applicationContext,
                MessageDB::class.java,
                "message_db.db")
                .build()
    }
}
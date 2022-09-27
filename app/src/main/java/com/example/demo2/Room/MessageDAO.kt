package com.example.demo2

import androidx.room.*

@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(message: Message):Long

    @Query("SELECT * FROM Messages")
    fun getAllMessages():List<Message>


}
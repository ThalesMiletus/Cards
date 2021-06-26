package com.example.cards.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cards.model.CardModel
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {

    @Query("SELECT * FROM card_table")
    fun getAllCards(): Flow<List<CardModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cardModel: CardModel)
}
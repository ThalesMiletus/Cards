package com.example.cards.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cards.model.CardModel

@Database(entities = [CardModel::class], version = 1, exportSchema = false)
abstract class CardRoomDatabase : RoomDatabase() {

    abstract fun cardDao(): CardDao
}
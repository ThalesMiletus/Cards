package com.example.cards.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cards.config.AppConfig
import com.example.cards.model.CardModel

@Database(entities = [CardModel::class], version = 1, exportSchema = false)
abstract class CardRoomDatabase : RoomDatabase() {

    abstract fun cardDao(): CardDao

    companion object {

        suspend fun populateDatabase(cardDao: CardDao) {
            for (i in 1..AppConfig.CARD_DEF_ITEM_COUNT) {
                cardDao.insert(
                    CardModel(
                        AppConfig.CARD_TITLE + " $i",
                        AppConfig.CARD_DEF_IMAGES_URL
                                + (i % AppConfig.CARD_DEF_IMAGES_COUNT)
                    )
                )
            }
        }
    }
}
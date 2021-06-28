package com.example.cards.util

import com.example.cards.config.AppConfig
import com.example.cards.model.CardModel
import com.example.cards.room.CardDao
import com.example.cards.room.CardRoomDatabase

suspend fun CardRoomDatabase.populateCardsDatabase(cardDao: CardDao) {
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
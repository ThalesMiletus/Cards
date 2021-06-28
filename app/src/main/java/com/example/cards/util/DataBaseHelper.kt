package com.example.cards.util

import com.example.cards.config.AppConfig
import com.example.cards.model.CardModel
import com.example.cards.room.CardRoomDatabase

suspend fun CardRoomDatabase.populateCardsDatabase() {
    for (i in AppConfig.CARD_DEF_ITEM_COUNT_START..AppConfig.CARD_DEF_ITEM_COUNT_END) {
        cardDao().insert(
            CardModel(
                AppConfig.CARD_TITLE + " $i",
                AppConfig.CARD_DEF_IMAGES_URL
                        + (i % AppConfig.CARD_DEF_IMAGES_COUNT)
            )
        )
    }
}
package com.example.cards.repository

import androidx.annotation.WorkerThread
import com.example.cards.model.CardModel
import com.example.cards.room.CardDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CardRepository @Inject constructor (private val cardDao: CardDao) {

    val allCards: Flow<List<CardModel>> = cardDao.getAllCards()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(card: CardModel) {
        cardDao.insert(card)
    }
}
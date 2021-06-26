package com.example.cards

import android.app.Application
import com.example.cards.repository.CardRepository
import com.example.cards.room.CardRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class CardsApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { CardRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { CardRepository(database.cardDao()) }
}
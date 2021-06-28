package com.example.cards.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.cards.config.AppConfig
import com.example.cards.room.CardRoomDatabase
import com.example.cards.ui.CardListAdapter
import com.example.cards.util.populateCardsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*

import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    lateinit var cardRoomDatabase: CardRoomDatabase

    @Provides
    @Singleton
    fun provideCardRoomDatabase(@ApplicationContext context: Context): CardRoomDatabase {
        cardRoomDatabase = Room.databaseBuilder(
            context,
            CardRoomDatabase::class.java,
            AppConfig.CARD_DB_NAME
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    CoroutineScope(SupervisorJob()).launch(Dispatchers.IO) {
                        cardRoomDatabase.populateCardsDatabase(cardRoomDatabase.cardDao())
                    }
                }
            })
            .build()
        return cardRoomDatabase
    }

    @Singleton
    @Provides
    fun provideCardDao(cardRoomDatabase: CardRoomDatabase) = cardRoomDatabase.cardDao()

    @Provides
    @Singleton
    fun provideAdapter(): CardListAdapter = CardListAdapter()
}
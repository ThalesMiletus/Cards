package com.example.cards.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.cards.config.AppConfig
import com.example.cards.model.CardModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [CardModel::class], version = 1, exportSchema = false)
abstract class CardRoomDatabase : RoomDatabase() {

    abstract fun cardDao(): CardDao

    companion object {
        @Volatile
        private var INSTANCE: CardRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): CardRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CardRoomDatabase::class.java,
                    "card_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(CardDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class CardDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.cardDao())
                    }
                }
            }
        }

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
package com.example.cards.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "card_table")
data class CardModel(

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "image")
    var imageUrl: String

) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null

}
package ru.mirea.barnatovich.shoppinglist.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "purchases")
data class Purchase(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val quantity: String,
    val note: String,
    @ColumnInfo(name = "is_bought") val isBought: Boolean = false
)

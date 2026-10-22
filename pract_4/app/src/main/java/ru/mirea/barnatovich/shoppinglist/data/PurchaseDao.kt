package ru.mirea.barnatovich.shoppinglist.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PurchaseDao {
    @Query("SELECT * FROM purchases ORDER BY id")
    fun getAll(): Flow<List<Purchase>>

    @Query("SELECT * FROM purchases WHERE id = :id")
    fun getById(id: Long): Flow<Purchase?>

    @Insert
    suspend fun insert(purchase: Purchase): Long

    @Insert
    suspend fun insertAll(purchases: List<Purchase>)

    @Update
    suspend fun update(purchase: Purchase)

    @Delete
    suspend fun delete(purchase: Purchase)
}

package com.bignerdranch.android.inventory.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    @Update
    suspend fun update(item: Item)

    @Delete
    suspend fun delete(item: Item)

    @Query("Select * from item WHERE id = :id")
    fun getItem(id:Int): Flow<Item>

    @Query("Select * from item ORDER BY name asc")
    fun getItems(): Flow<List<Item>>
}
package com.xavier.calculator.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ComputeDao {

    @Insert(onConflict = OnConflictStrategy.NONE)
    suspend fun insert(entity: ComputeEntity)

    @Query("DELETE FROM ComputeEntity where id NOT IN (SELECT id from ComputeEntity ORDER BY id DESC LIMIT 10)")
    suspend fun deleteOld()

    @Query("SELECT * FROM ComputeEntity ORDER BY id DESC LIMIT 10")
    fun history(): Flow<List<ComputeEntity>>

    @Query("DELETE FROM ComputeEntity")
    suspend fun clear()
}
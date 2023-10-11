package com.neirno.tv_client.data.data_source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.neirno.tv_client.data.data_source.entity.History
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Query("SELECT * FROM History")
    fun getHistories(): Flow<List<History>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(connection: History): Long

    @Query("SELECT * FROM History WHERE id = :id")
    suspend fun getHistoryById(id: Long): History?

    @Query("DELETE FROM History WHERE id = :id")
    suspend fun deleteHistory(id: Long)
}
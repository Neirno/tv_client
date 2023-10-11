package com.neirno.tv_client.data.data_source

import androidx.room.*
import com.neirno.tv_client.data.data_source.entity.Connection
import kotlinx.coroutines.flow.Flow

@Dao
interface ConnectionDao {
    @Query("SELECT * FROM Connection")
    fun getConnections(): Flow<List<Connection>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConnection(connection: Connection): Long

    @Query("SELECT * FROM Connection WHERE id = :id")
    suspend fun getConnectionById(id: Long): Connection?

    @Query("DELETE FROM Connection WHERE id = :id")
    suspend fun deleteConnection(id: Long)
}
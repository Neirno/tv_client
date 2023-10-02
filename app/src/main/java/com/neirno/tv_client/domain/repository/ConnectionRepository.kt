package com.neirno.tv_client.domain.repository

import com.neirno.tv_client.domain.entity.Connection
import kotlinx.coroutines.flow.Flow

interface ConnectionRepository {

    fun getConnections(): Flow<List<Connection>>

    suspend fun saveConnection(connection: Connection)

    suspend fun getConnectionById(id: Long): Connection?

    suspend fun deleteConnection(id: Long)

    suspend fun checkAndSaveConnection(ip: String): Boolean

    suspend fun checkConnection(ip: String): Boolean

    suspend fun setInterceptorUrl(ip: String)
}

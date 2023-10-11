package com.neirno.tv_client.domain.use_case.connection

import com.neirno.tv_client.domain.entity.Connection
import com.neirno.tv_client.domain.repository.ConnectionRepository
import kotlinx.coroutines.flow.Flow

class GetConnections (
    private val connectionRepository: ConnectionRepository
) {
     operator fun invoke(): Flow<List<Connection>> {
        return connectionRepository.getConnections()
    }
}
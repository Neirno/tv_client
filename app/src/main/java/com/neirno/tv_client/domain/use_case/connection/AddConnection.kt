package com.neirno.tv_client.domain.use_case.connection

import com.neirno.tv_client.domain.entity.Connection
import com.neirno.tv_client.domain.repository.ConnectionRepository

class AddConnection (
    private val connectionRepository: ConnectionRepository
) {
    suspend operator fun invoke(connection: Connection) {
        connectionRepository.saveConnection(connection)
    }
}
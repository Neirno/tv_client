package com.neirno.tv_client.domain.use_case.connection

import com.neirno.tv_client.domain.repository.ConnectionRepository

class DeleteConnection (
    private val connectionRepository: ConnectionRepository
) {
    suspend operator fun invoke(id: Long) {
        connectionRepository.deleteConnection(id)
    }
}
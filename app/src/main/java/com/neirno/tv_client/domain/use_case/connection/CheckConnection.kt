package com.neirno.tv_client.domain.use_case.connection

import com.neirno.tv_client.core.util.isValidIP
import com.neirno.tv_client.domain.repository.ConnectionRepository

class CheckConnection (
    private val connectionRepository: ConnectionRepository
) {
    suspend operator fun invoke(ip: String): Boolean {
        if (!isValidIP(ip)) {
            return false
        }
        return connectionRepository.checkConnection(ip).also { success ->
            if (success) {
                connectionRepository.setInterceptorUrl(ip)
            }
        }
    }
}
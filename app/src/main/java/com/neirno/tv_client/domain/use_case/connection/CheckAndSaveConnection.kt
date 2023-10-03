package com.neirno.tv_client.domain.use_case.connection

import com.neirno.tv_client.core.util.isValidIP
import com.neirno.tv_client.domain.repository.ConnectionRepository
import com.neirno.tv_client.core.network.Result


class CheckAndSaveConnection(
    private val connectionRepository: ConnectionRepository,
) {
    suspend operator fun invoke(ip: String): Result<Boolean> {
        if (!isValidIP(ip)) {
            return Result.Error(IllegalArgumentException("Invalid IP"))
        }

        val connectionResult = connectionRepository.checkAndSaveConnection(ip)
        return when (connectionResult) {
            is Result.Success -> {
                connectionRepository.setInterceptorUrl(ip)
                Result.Success(connectionResult.data)
            }
            is Result.Error -> connectionResult
        }
    }

}

package com.neirno.tv_client.domain.use_case.power

import com.neirno.tv_client.domain.repository.PowerRepository
import com.neirno.tv_client.core.network.Result

class LightOn(private val repository: PowerRepository) {
    suspend operator fun invoke(): Result<Boolean> = repository.lightOn()
}

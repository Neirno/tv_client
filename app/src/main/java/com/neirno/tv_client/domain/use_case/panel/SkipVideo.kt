package com.neirno.tv_client.domain.use_case.panel

import com.neirno.tv_client.core.network.Result
import com.neirno.tv_client.domain.repository.PanelRepository

class SkipVideo(private val repository: PanelRepository) {
    suspend operator fun invoke(): Result<Boolean> = repository.skip()
}
package com.neirno.tv_client.domain.use_case.panel

import com.neirno.tv_client.domain.repository.PanelRepository
import com.neirno.tv_client.core.network.Result

class SetVolume(private val repository: PanelRepository) {
    suspend operator fun invoke(volume: Int): Result<Boolean> {
        val adjustedVolume = volume.coerceIn(0, 100)
        return repository.setVolume(adjustedVolume.toString())
    }
}

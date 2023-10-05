package com.neirno.tv_client.domain.use_case.panel

import com.neirno.tv_client.domain.repository.PanelRepository
import com.neirno.tv_client.core.network.Result

class VolumeShiftForward(private val repository: PanelRepository) {
    suspend operator fun invoke(): Result<Boolean> = repository.volumeShiftForward()
}

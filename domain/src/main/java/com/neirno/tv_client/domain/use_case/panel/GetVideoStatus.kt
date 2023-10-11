package com.neirno.tv_client.domain.use_case.panel

import com.neirno.tv_client.core.network.Result
import com.neirno.tv_client.domain.entity.VideoStatus
import com.neirno.tv_client.domain.repository.PanelRepository

class GetVideoStatus(private val panelRepository: PanelRepository) {
    suspend operator fun invoke(): Result<VideoStatus> {
        return panelRepository.getStatus()
    }
}

package com.neirno.tv_client.domain.use_case.youtube

import com.neirno.tv_client.core.network.Result
import com.neirno.tv_client.domain.entity.Youtube
import com.neirno.tv_client.domain.repository.YoutubeRepository

class SendVideoUrl (
    private val youtubeRepository: YoutubeRepository
) {
    suspend operator fun invoke(video: Youtube): Result<Boolean> {
        return youtubeRepository.sendVideoUrl(video.videoUrl)
    }
}
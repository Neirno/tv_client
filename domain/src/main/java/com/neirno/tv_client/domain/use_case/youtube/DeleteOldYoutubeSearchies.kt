package com.neirno.tv_client.domain.use_case.youtube

import com.neirno.tv_client.domain.repository.YoutubeRepository

class DeleteOldYoutubeSearchies(
    private val youtubeRepository: YoutubeRepository
) {
    suspend operator fun invoke(limit: Int = 50) {
        youtubeRepository.deleteOldQueries(limit)
    }
}
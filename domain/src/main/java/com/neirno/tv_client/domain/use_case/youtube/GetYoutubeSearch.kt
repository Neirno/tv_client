package com.neirno.tv_client.domain.use_case.youtube

import com.neirno.tv_client.domain.entity.YoutubeSearch
import com.neirno.tv_client.domain.repository.YoutubeRepository

class GetYoutubeSearch(
    private val youtubeRepository: YoutubeRepository
) {
    suspend operator fun invoke(id: Long): YoutubeSearch? {
        return youtubeRepository.getYoutubeSearchById(id)
    }
}
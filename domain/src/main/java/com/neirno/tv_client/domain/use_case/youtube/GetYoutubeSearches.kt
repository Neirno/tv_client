package com.neirno.tv_client.domain.use_case.youtube

import com.neirno.tv_client.domain.entity.YoutubeSearch
import com.neirno.tv_client.domain.repository.YoutubeRepository
import kotlinx.coroutines.flow.Flow

class GetYoutubeSearches(
    private val youtubeRepository: YoutubeRepository
) {
    operator fun invoke(): Flow<List<YoutubeSearch>> {
        return youtubeRepository.getYoutubeSearches()
    }
}
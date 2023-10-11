package com.neirno.tv_client.domain.use_case.youtube

import com.neirno.tv_client.core.network.Result
import com.neirno.tv_client.domain.entity.Youtube
import com.neirno.tv_client.domain.repository.YoutubeRepository

class SearchVideo (
    private val youtubeRepository: YoutubeRepository
) {
    suspend operator fun invoke(query: String, offset: Int, limit: Int): Result<List<Youtube>> {
        return youtubeRepository.searchVideo(query, offset, limit)
    }
}
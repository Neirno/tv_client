package com.neirno.tv_client.domain.use_case.youtube

import com.neirno.tv_client.domain.entity.YoutubeSearch
import com.neirno.tv_client.domain.repository.YoutubeRepository

class InsertYoutubeSearch(
    private val youtubeRepository: YoutubeRepository
) {
    suspend operator fun invoke(youtubeSearch: YoutubeSearch) {
        val query = youtubeSearch.query
        youtubeRepository.getYoutubeSearches().collect { searchList ->
            if (searchList.none { it.query == query }) {
                youtubeRepository.saveYoutubeSearch(youtubeSearch)
            }
        } // Проверка, существует ли это значение
    }
}
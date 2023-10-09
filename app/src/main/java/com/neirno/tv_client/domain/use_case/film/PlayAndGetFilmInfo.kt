package com.neirno.tv_client.domain.use_case.film

import com.neirno.tv_client.core.network.Result
import com.neirno.tv_client.domain.entity.FilmInfo
import com.neirno.tv_client.domain.repository.FilmRepository

class PlayAndGetFilmInfo (
    private val filmRepository: FilmRepository
) {
    suspend operator fun invoke(category: String, film: String): Result<FilmInfo> {
        return filmRepository.playAndGetFilmInfo(category, film)
    }
}
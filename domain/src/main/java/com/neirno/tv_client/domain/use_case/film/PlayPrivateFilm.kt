package com.neirno.tv_client.domain.use_case.film

import com.neirno.tv_client.domain.entity.FilmInfo
import com.neirno.tv_client.domain.repository.FilmRepository
import com.neirno.tv_client.core.network.Result

class PlayPrivateFilm(
    private val filmRepository: FilmRepository
) {
    suspend operator fun invoke(filmName: String): Result<FilmInfo> {
        return filmRepository.playPrivateFilm(filmName)
    }
}

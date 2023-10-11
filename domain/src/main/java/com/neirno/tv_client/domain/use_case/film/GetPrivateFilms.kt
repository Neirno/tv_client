package com.neirno.tv_client.domain.use_case.film

import com.neirno.tv_client.core.network.Result
import com.neirno.tv_client.domain.repository.FilmRepository

class GetPrivateFilms(
    private val filmRepository: FilmRepository
) {
    suspend operator fun invoke(password: String): Result<List<String>> {
        return filmRepository.getPrivateFilms(password)
    }
}


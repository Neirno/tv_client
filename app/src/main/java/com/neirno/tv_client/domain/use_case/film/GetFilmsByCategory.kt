package com.neirno.tv_client.domain.use_case.film

import com.neirno.tv_client.core.network.Result
import com.neirno.tv_client.domain.repository.FilmRepository

class GetFilmsByCategory(
    private val filmRepository: FilmRepository
) {
    suspend operator fun invoke(category: String): Result<List<String>> {
        return filmRepository.getFilmsByCategory(category)
    }
}
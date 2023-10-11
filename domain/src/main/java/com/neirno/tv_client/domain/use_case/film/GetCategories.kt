package com.neirno.tv_client.domain.use_case.film

import com.neirno.tv_client.core.network.Result
import com.neirno.tv_client.domain.repository.FilmRepository

class GetCategories(
    private val filmRepository: FilmRepository
) {
    suspend operator fun invoke(): Result<List<String>> {
        return filmRepository.getCategories()
    }
}
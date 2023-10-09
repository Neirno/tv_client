package com.neirno.tv_client.domain.use_case.film

data class FilmUseCase(
    val getCategories: GetCategories,
    val getFilmsByCategory: GetFilmsByCategory,
    val playAndGetFilmInfo: PlayAndGetFilmInfo
)

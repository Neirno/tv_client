package com.neirno.tv_client.data.api.model

import com.neirno.tv_client.domain.entity.FilmInfo

data class FilmInfoResponse(
    val category: String,
    val filmName: String,
    val filmSize: Long,
    val filmPath: String
)

fun FilmInfoResponse.toDomain(): FilmInfo {
    return FilmInfo(
        category = this.category,
        filmName = this.filmName,
        filmSize = this.filmSize,
        filmPath = this.filmPath
    )
}
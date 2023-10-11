package com.neirno.tv_client.data.api.model

import com.neirno.tv_client.domain.entity.Youtube

data class YoutubeSearchResponse(
    val title: String,
    val channel: String,
    val views: Int,
    val duration: String,
    val thumbnailUrl: String,
    val videoUrl: String
)

fun YoutubeSearchResponse.toDomain(): Youtube {
    return Youtube(
        title = this.title,
        channel = this.channel,
        views = this.views,
        duration = this.duration,
        thumbnailUrl = this.thumbnailUrl,
        videoUrl = this.videoUrl
    )
}

package com.neirno.tv_client.data.api.model

import com.neirno.tv_client.domain.entity.Youtube

data class YoutubeResponse(
    val title: String,
    val channel: String,
    val views: Int,
    val description: String,
    val thumbnailUrl: String,
    val videoUrl: String
)

fun YoutubeResponse.toDomain(): Youtube {
    return Youtube(
        title = this.title,
        channel = this.channel,
        views = this.views,
        description = this.description,
        thumbnailUrl = this.thumbnailUrl,
        videoUrl = this.videoUrl
    )
}

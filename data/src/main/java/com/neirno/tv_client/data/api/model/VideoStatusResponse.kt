package com.neirno.tv_client.data.api.model

import com.neirno.tv_client.domain.entity.VideoStatus

data class VideoStatusResponse(
    val result: String,
    val message: String,
    val title: String,
    val channel: String,
    val views: String,
    val duration: String,
    val thumbnail_url: String,
    val current_time: String,
    val volume: Int
)

fun VideoStatusResponse.toDomain() : VideoStatus{
    return VideoStatus(
        result = this.result,
        message = this.message,
        title = this.title,
        channel = this.channel,
        views = this.views,
        duration = this.duration,
        thumbnail_url = this.thumbnail_url,
        current_time = this.current_time,
        volume = this.volume
    )
}

package com.neirno.tv_client.domain.entity

data class VideoStatus (
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
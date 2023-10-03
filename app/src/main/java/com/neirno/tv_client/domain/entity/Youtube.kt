package com.neirno.tv_client.domain.entity

data class Youtube(
    val title: String,
    val channel: String,
    val views: Int,
    val duration: String,
    val thumbnailUrl: String,
    val videoUrl: String
)
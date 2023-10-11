package com.neirno.tv_client.data.data_source.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Connection(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val ip: String
)

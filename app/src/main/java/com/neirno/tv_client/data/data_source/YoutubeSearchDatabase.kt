package com.neirno.tv_client.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.neirno.tv_client.data.data_source.entity.Connection
import com.neirno.tv_client.data.data_source.entity.YoutubeSearch


@Database(
    entities = [YoutubeSearch::class],
    version = 1
)
abstract class YoutubeSearchDatabase: RoomDatabase() {

    abstract val youtubeSearchDao: YoutubeSearchDao

    companion object {
        const val DATABASE_NAME = "youtube_search_db"
    }
}
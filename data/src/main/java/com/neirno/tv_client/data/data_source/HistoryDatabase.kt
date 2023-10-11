package com.neirno.tv_client.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.neirno.tv_client.data.data_source.entity.History

@Database(
    entities = [History::class],
    version = 1
)
abstract class HistoryDatabase: RoomDatabase() {

    abstract val historyDao: HistoryDao

    companion object {
        const val DATABASE_NAME = "history_db"
    }
}
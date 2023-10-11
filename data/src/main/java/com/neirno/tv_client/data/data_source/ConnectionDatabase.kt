package com.neirno.tv_client.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.neirno.tv_client.data.data_source.entity.Connection

@Database(
    entities = [Connection::class],
    version = 1
)
abstract class ConnectionDatabase: RoomDatabase() {

    abstract val connectionDao: ConnectionDao

    companion object {
        const val DATABASE_NAME = "connection_db"
    }
}
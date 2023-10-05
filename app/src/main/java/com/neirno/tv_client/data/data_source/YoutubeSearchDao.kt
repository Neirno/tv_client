package com.neirno.tv_client.data.data_source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.neirno.tv_client.data.data_source.entity.YoutubeSearch
import kotlinx.coroutines.flow.Flow

@Dao
interface YoutubeSearchDao {
    @Query("SELECT * FROM YoutubeSearch")
    fun getYoutubeSearches(): Flow<List<YoutubeSearch>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertYoutubeSearch(youtubeSearch: YoutubeSearch): Long

    @Query("SELECT * FROM YoutubeSearch WHERE id = :id")
    suspend fun getYoutubeSearchById(id: Long): YoutubeSearch?

    @Query("DELETE FROM YoutubeSearch WHERE id = :id")
    suspend fun deleteYoutubeSearch(id: Long)

    @Query("""
        DELETE FROM YoutubeSearch
        WHERE id NOT IN (SELECT id FROM YoutubeSearch ORDER BY id DESC LIMIT :limit)
    """)
    suspend fun deleteOldQueries(limit: Int)

}
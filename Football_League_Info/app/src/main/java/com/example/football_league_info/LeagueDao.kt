package com.example.football_league_info

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LeagueDao {
    @Query("select * from league")
    suspend fun getAll(): List<League>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg users: League)
    // insert one user without replacing an identical one - duplicates allowed
    @Insert
    suspend fun insertUser(user: League)
    @Delete
    suspend fun deleteUser(user: League)
    @Query("select * from league where strLeague LIKE :name")
    fun findByLastName(name: String): League
    @Query("delete from league")
    suspend fun deleteAll()
}
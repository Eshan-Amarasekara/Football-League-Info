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
    suspend fun insertAll(vararg leagues: League)
    @Insert
    suspend fun insertUser(league: League)
    @Delete
    suspend fun deleteUser(league: League)
    @Query("select * from league where strLeague LIKE :name")
    fun findByLastName(name: String): League
    @Query("delete from league")
    suspend fun deleteAll()

    @Query("select * from league where strLeagueAlternate LIKE :LeagueAlternate")
    fun findByLeagueAlternate(LeagueAlternate: String): League
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllLeagues(vararg allLeagues: AllLeague)

    @Query("SELECT name " +
            "FROM AllLeague " +
            "WHERE LOWER(strLeague) LIKE LOWER('%' || :keyword || '%') " +
            "OR LOWER(Name) LIKE LOWER('%' || :keyword || '%')")
    suspend fun searchForLeaguesName(keyword: String): List<String>


    @Query("SELECT strTeamLogo " +
            "FROM AllLeague" +
            " WHERE LOWER(strLeague) LIKE LOWER('%' || :keyword || '%') " +
            "OR LOWER(Name) LIKE LOWER('%' || :keyword || '%')")
    suspend fun strTeamLogo (keyword: String): List<String>
}
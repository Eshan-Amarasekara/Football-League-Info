package com.example.football_league_info

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [League::class,AllLeague::class], version=1)
abstract class LeagueDatabase: RoomDatabase() {
    abstract fun leagueDao():LeagueDao
}
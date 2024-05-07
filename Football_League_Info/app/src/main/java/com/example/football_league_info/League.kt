package com.example.football_league_info

import androidx.room.Entity
import androidx.room.PrimaryKey

//League entity
@Entity
data class League(
    @PrimaryKey(autoGenerate = true) val idLeague: Int,
    val strLeague: String?,
    val strSport: String?,
    val strLeagueAlternate: String?

)

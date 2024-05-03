package com.example.football_league_info

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class League(
    @PrimaryKey(autoGenerate = true) var id: Int=0,
    val idLeague: Int?,
    val strLeague: String?,
    val strSport: String?,
    val strLeagueAlternate: String?

)

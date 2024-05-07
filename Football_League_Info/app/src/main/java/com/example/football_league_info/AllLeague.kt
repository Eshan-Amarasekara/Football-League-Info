package com.example.football_league_info

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AllLeague(
    @PrimaryKey(autoGenerate = true) val idTeam: Int = 0,
    val name: String?,
    val strTeamShort: String?,
    val strAlternate: String?,
    val intFormedYear: String?,
    val strLeague: String?,
    val idLeague: String?,
    val strStadium: String?,
    val strKeywords: String?,
    val strStadiumThumb: String?,
    val strStadiumLocation: String?,
    val intStadiumCapacity: String?,
    val strWebsite: String?,
    val strTeamJersey: String?,
    val strTeamLogo: String?,
)


package com.example.football_league_info

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import com.example.football_league_info.ui.theme.Football_League_InfoTheme

lateinit var db: LeagueDatabase
lateinit var leagueDao: LeagueDao

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = Room.databaseBuilder(this,
            LeagueDatabase::class.java, "LeagueDatabase").build()
        leagueDao = db.leagueDao()

        setContent {

        }
    }
}


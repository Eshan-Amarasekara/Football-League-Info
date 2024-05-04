package com.example.football_league_info

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.room.Room
import kotlinx.coroutines.launch

lateinit var db: LeagueDatabase
lateinit var leagueDao: LeagueDao

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = Room.databaseBuilder(this,
            LeagueDatabase::class.java, "LeagueDatabase").build()
        leagueDao = db.leagueDao()

        setContent {
            GUI()
        }
    }
}

@Composable
fun GUI() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(
            modifier = Modifier.height(16.dp)
        )
        val context = LocalContext.current

        val scope = rememberCoroutineScope()
        var message by rememberSaveable { mutableStateOf<String>("") }

        Row() {
            Button(
                onClick = {
                    scope.launch {
                        leagueDao.insertAll(
                            League(4328, "English Premier League", "Soccer", "Premier League, EPL"),
                            League(4329, "English League Championship", "Soccer", "Championship"),
                            League(
                                4330,
                                "Scottish Premier League",
                                "Soccer",
                                "Scottish Premiership, SPFL"
                            ),
                            League(
                                4331,
                                "German Bundesliga",
                                "Soccer",
                                "Bundesliga, Fußball-Bundesliga"
                            ),
                            League(4332, "Italian Serie A", "Soccer", "Serie A"),
                            League(4334, "French Ligue 1", "Soccer", "Ligue 1 Conforama"),
                            League(4335, "Spanish La Liga", "Soccer", "LaLiga Santander, La Liga"),
                            League(4336, "Greek Superleague Greece", "Soccer", ""),
                            League(4337, "Dutch Eredivisie", "Soccer", "Eredivisie"),
                            League(
                                4338,
                                "Belgian First Division A",
                                "Soccer",
                                "Jupiler Pro League"
                            ),
                            League(4339, "Turkish Super Lig", "Soccer", "Super Lig"),
                            League(4340, "Danish Superliga", "Soccer", ""),
                            League(4344, "Portuguese Primeira Liga", "Soccer", "Liga NOS"),
                            League(
                                4346,
                                "American Major League Soccer",
                                "Soccer",
                                "MLS, Major League Soccer"
                            ),
                            League(4347, "Swedish Allsvenskan", "Soccer", "Fotbollsallsvenskan"),
                            League(4350, "Mexican Primera League", "Soccer", "Liga MX"),
                            League(4351, "Brazilian Serie A", "Soccer", ""),
                            League(4354, "Ukrainian Premier League", "Soccer", ""),
                            League(
                                4355,
                                "Russian Football Premier League",
                                "Soccer",
                                "Чемпионат России по футболу"
                            ),
                            League(4356, "Australian A-League", "Soccer", "A-League"),
                            League(4358, "Norwegian Eliteserien", "Soccer", "Eliteserien"),
                            League(4359, "Chinese Super League", "Soccer", "")
                        )
                    }
                    message = "Leagues added to Database"
                }, modifier = Modifier
                    .width(130.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color.Black),
                border = BorderStroke(3.dp, Color(235, 127, 0)),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(Color(235, 127, 0))
            )
            {
                Text(text = "Add Leagues to DB")
            }

        }
        Spacer(modifier = Modifier.height(16.dp))
        Row {


            Button(
                onClick = {
                    message = ""
                    var i = Intent(context, SearchClubsByLeague::class.java)
                    context.startActivity(i)
                }, modifier = Modifier
                    .width(130.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color.Black),
                border = BorderStroke(3.dp, Color(235, 127, 0)),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(Color(235, 127, 0))
            )
            {
                Text(text = "Search for Clubs By League")
            }
        }
        Spacer(modifier = Modifier.height(18.dp))
        Row {


            Button(
                onClick = {
                    message = ""
                }, modifier = Modifier
                    .width(130.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color.Black),
                border = BorderStroke(3.dp, Color(235, 127, 0)),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(Color(235, 127, 0))
            )
            {
                Text(text = "Search for Clubs")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Text(text = message)
        }

    }
}
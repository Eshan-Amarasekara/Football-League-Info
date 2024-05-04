package com.example.football_league_info

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.football_league_info.ui.theme.Football_League_InfoTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class SearchClubsByLeague : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GUIButton2()
        }
    }
}

@Composable
fun GUIButton2() {
    Column {
        val scope = rememberCoroutineScope()
        var keyword by rememberSaveable { mutableStateOf("") }
        var leagueInfoList by remember { mutableStateOf<List<LeagueInfo>>(emptyList()) }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            TextField(
                value = keyword,
                onValueChange = { keyword = it },
                label = { Text("Enter Keyword") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    scope.launch {
                        leagueInfoList = fetchLeagueDetails(keyword)
                    }
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Search")
            }
        }

        LazyColumn {
            items(leagueInfoList) { leagueInfo ->
                LeagueInfoItem(leagueInfo)
                Divider()
            }
        }
    }
}


@Composable
fun LeagueInfoItem(leagueInfo: LeagueInfo) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text("idTeam:${leagueInfo.idTeam}")
        Text("Name: ${leagueInfo.name}")
        Text("strTeamShort: ${leagueInfo.strTeamShort}")
        Text("strAlternate: ${leagueInfo.strAlternate}")
        Text("intFormedYear: ${leagueInfo.intFormedYear}")
        Text("strLeague: ${leagueInfo.strLeague}")
        Text("strStadium: ${leagueInfo.strStadium}")
        Text("strKeywords: ${leagueInfo.strKeywords}")
        Text("strStadiumThumb: ${leagueInfo.strStadiumThumb}")
        Text("intStadiumCapacity: ${leagueInfo.intStadiumCapacity}")
        Text("strWebsite: ${leagueInfo.strWebsite}")
        Text("strTeamJersey: ${leagueInfo.strTeamJersey}")
        Text("strTeamLogo: ${leagueInfo.strTeamLogo}")

    }
}

data class LeagueInfo(
    val idTeam: String,
    val name: String,
    val strTeamShort: String,
    val strAlternate: String,
    val intFormedYear: String,
    val strLeague: String,
    val strStadium: String,
    val strKeywords: String,
    val strStadiumThumb: String,
    val strStadiumLocation: String,
    val intStadiumCapacity: String,
    val strWebsite: String,
    val strTeamJersey: String,
    val strTeamLogo: String
)

suspend fun fetchLeagueDetails(keyword: String): List<LeagueInfo> {
    val url_string = "https://www.thesportsdb.com/api/v1/json/3/search_all_leagues.php?c=$keyword&s=Soccer"
    val url = URL(url_string)
    val con: HttpURLConnection = url.openConnection() as HttpURLConnection
    var stb = StringBuilder()

    withContext(Dispatchers.IO) {
        var bf = BufferedReader(InputStreamReader(con.inputStream))
        var line: String? = bf.readLine()
        while (line != null) {
            stb.append(line + "\n")
            line = bf.readLine()
        }
    }

    return parseLeagueJSON(stb)
}

fun parseLeagueJSON(stb: StringBuilder): List<LeagueInfo> {
    val jsonArray = JSONArray(stb.toString())
    val leagueInfoList = mutableListOf<LeagueInfo>()

    for (i in 0 until jsonArray.length()) {
        val jsonObject = jsonArray.getJSONObject(i)
        val idTeam = jsonObject.getString("idLeague")
        val name = jsonObject.getString("strTeam")
        val strTeamShort = jsonObject.getString("strTeamShort")
        val strAlternate = jsonObject.getString("strAlternate")
        val intFormedYear = jsonObject.getString("intFormedYear")
        val strLeague = jsonObject.getString("strLeague")
        val strStadium = jsonObject.getString("strStadium")
        val strKeywords = jsonObject.getString("strKeywords")
        val strStadiumThumb = jsonObject.getString("strStadiumThumb")
        val strStadiumLocation = jsonObject.getString("strStadiumLocation")
        val intStadiumCapacity = jsonObject.getString("intStadiumCapacity")
        val strWebsite = jsonObject.getString("strWebsite")
        val strTeamJersey = jsonObject.getString("strTeamJersey")
        val strTeamLogo = jsonObject.getString("strTeamLogo")


        val leagueInfo = LeagueInfo(
            idTeam = idTeam,
            name= name,
            strTeamShort = strTeamShort,
            strAlternate = strAlternate,
            intFormedYear = intFormedYear,
            strLeague = strLeague,
            strStadium = strStadium,
            strKeywords = strKeywords,
            strStadiumThumb = strStadiumThumb,
            strStadiumLocation = strStadiumLocation,
            intStadiumCapacity = intStadiumCapacity,
            strWebsite = strWebsite,
            strTeamJersey = strTeamJersey,
            strTeamLogo = strTeamLogo
        )
        leagueInfoList.add(leagueInfo)
    }

    return leagueInfoList
}

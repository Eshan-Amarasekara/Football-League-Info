package com.example.football_league_info

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
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
        var displayLeagues by rememberSaveable { mutableStateOf("") }
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
                        displayLeagues = fetchLeagueDetails(keyword)
                    }
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Search")
            }

            Text(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                text = displayLeagues
            )
        }
    }
}

suspend fun fetchLeagueDetails(keyword: String): String {
    val urlString = "https://www.thesportsdb.com/api/v1/json/3/search_all_teams.php?l="+keyword
    val url = URL(urlString)
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
    val allLeagues = parseJSON(stb)
    return allLeagues
}

fun parseJSON(stb: StringBuilder): String {

    val json = JSONObject(stb.toString())
    val allLeagues = StringBuilder()
    var jsonArray: JSONArray = json.getJSONArray("teams")

    for (i in 0 until jsonArray.length()) {
        val teams: JSONObject = jsonArray[i] as JSONObject

        try {
            val idTeam = teams["idTeam"] as String
            val name = teams["strTeam"] as String
            val strTeamShort = teams["strTeamShort"] as String
            val strAlternate = teams["strAlternate"] as String
            val intFormedYear = teams["intFormedYear"] as String
            val strLeague = teams["strLeague"] as String
            val strStadium = teams["strStadium"] as String
            val strKeywords = teams["strKeywords"] as String
            val strStadiumThumb = teams["strStadiumThumb"] as String
            val strStadiumLocation = teams["strStadiumLocation"] as String
            val intStadiumCapacity = teams["intStadiumCapacity"] as String
            val strWebsite = teams["strWebsite"] as String
            val strTeamJersey = teams["strTeamJersey"] as String
            val strTeamLogo = teams["strTeamLogo"] as String

            allLeagues.append("idTeam: $idTeam\n")
            allLeagues.append("name: $name\n")
            allLeagues.append("strTeamShort: $strTeamShort\n")
            allLeagues.append("strAlternate: $strAlternate\n")
            allLeagues.append("intFormedYear: $intFormedYear\n")
            allLeagues.append("strLeague: $strLeague\n")
            allLeagues.append("strStadium: $strStadium\n")
            allLeagues.append("strKeywords: $strKeywords\n")
            allLeagues.append("strStadiumThumb: $strStadiumThumb\n")
            allLeagues.append("strStadiumLocation: $strStadiumLocation\n")
            allLeagues.append("intStadiumCapacity: $intStadiumCapacity\n")
            allLeagues.append("strWebsite: $strWebsite\n")
            allLeagues.append("strTeamJersey: $strTeamJersey\n")
            allLeagues.append("strTeamLogo: $strTeamLogo\n")
        }catch (jen: JSONException){

        }
        allLeagues.append("\n\n")
    }

    return allLeagues.toString()
}

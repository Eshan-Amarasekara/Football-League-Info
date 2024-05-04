package com.example.football_league_info

import android.os.Bundle
import android.util.Log
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import org.json.JSONException
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

    val json = JSONArray(stb.toString())
    val allLeagues = StringBuilder()

    for (i in 0 until json.length()) {
        val jsonObject = json.getJSONObject(i)
        try {
            val idTeam = jsonObject.getString("idTeam")
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
            Log.d("Error","Error")
        }
    }

    return allLeagues.toString()
}

package com.example.football_league_info

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.room.Room
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

lateinit var db2: LeagueDatabase
lateinit var leagueDao2: LeagueDao
class SearchClubsByLeague : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            db2 = Room.databaseBuilder(
                this,LeagueDatabase::class.java, "Leagues").build()
            leagueDao2 = db2.leagueDao()
            GUIButton2()
        }
    }
}

@Composable
fun GUIButton2() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val scope = rememberCoroutineScope()
        var keyword by rememberSaveable { mutableStateOf("") }
        var displayLeagues by rememberSaveable { mutableStateOf("") }
        var mode by rememberSaveable {mutableStateOf("" )}
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        )
        //Input keyword area
        {
            TextField(
                value = keyword,
                onValueChange = { keyword = it },
                label = { Text("Enter Keyword") },
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color(235, 127, 0) ,
                    focusedContainerColor = Color(235, 127, 0).copy(alpha = 0.2f),
                    focusedLabelColor = Color(235, 127, 0),
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        //Retrieve Button
        Row{
            Button(
                onClick = {
                    scope.launch {
                        mode = "retrieve"
                        displayLeagues = fetchLeagueDetails(mode,keyword)
                    }
                },
                modifier = Modifier
                    .padding(top = 8.dp)
                    .width(130.dp),
                border = BorderStroke(3.dp, Color(235, 127, 0)),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(Color(235, 127, 0))
            ) {
                Text("Retrieve Clubs")
            }
        }

        //Save to DB button
        Row{
            Button(
                onClick = {
                    if(mode=="retrieve") {
                        scope.launch {
                            mode = "save"
                            fetchLeagueDetails(mode, keyword)
                        }
                    }else{
                        Log.d("No clubs", "No clubs received")
                    }
                },
                modifier = Modifier
                    .padding(top = 8.dp)
                    .width(130.dp),
                border = BorderStroke(3.dp, Color(235, 127, 0)),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(Color(235, 127, 0))
            ) {
                Text("Save Clubs")
            }
        }
        Row{
            Spacer(modifier = Modifier.width(16.dp))
            if(mode=="retrieve") {
                Text(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    text = displayLeagues
                )
            }else if(mode=="save"){
                Text(text = "Retrieved clubs saved to database")
            }
        }
    }
}

//Function to fetch all league details/ also includes saving to DB
suspend fun fetchLeagueDetails(mode:String, keyword: String): String {
    val urlString = "https://www.thesportsdb.com/api/v1/json/3/search_all_teams.php?l="+keyword
    val url = URL(urlString)
    val con: HttpURLConnection = url.openConnection() as HttpURLConnection
    var stb = StringBuilder()

    //Saving all lines in the JSON to string builder
    withContext(Dispatchers.IO) {
        var bf = BufferedReader(InputStreamReader(con.inputStream))
        var line: String? = bf.readLine()
        while (line != null) {
            stb.append(line + "\n")
            line = bf.readLine()
        }
    }
    var output = ""
    if(mode=="retrieve"){
        output = parseJSON(stb)
    }else{
        output= parseDB(stb).toString()
    }

    return output
}

//parseJSON method to retrieve league information
fun parseJSON(stb: StringBuilder): String {

    val json = JSONObject(stb.toString())
    val allLeagues = StringBuilder()
    var jsonArray: JSONArray = json.getJSONArray("teams")

    //Iterating through the saved string builder, error handling added to avoid null value errors
    for (i in 0 until jsonArray.length()) {
        try {
        val teams: JSONObject = jsonArray[i] as JSONObject

            try {
                val idTeam = teams["idTeam"] as String
                allLeagues.append("idTeam: $idTeam\n")
            }catch(e: Throwable){
                allLeagues.append("idTeam: -\n")
            }

            try {
                val name = teams["strTeam"] as String
                allLeagues.append("name: $name\n")

            }catch(e:Throwable){
                allLeagues.append("name: -\n")

            }

            try {
                val strTeamShort = teams["strTeamShort"] as String
                allLeagues.append("strTeamShort: $strTeamShort\n")

            }catch(e:Throwable){
                allLeagues.append("strTeamShort: -\n")

            }

            try {
                val strAlternate = teams["strAlternate"] as String
                allLeagues.append("strAlternate: $strAlternate\n")

            }catch(e:Throwable){
                allLeagues.append("strAlternate: -\n")

            }
            try{
                val intFormedYear = teams["intFormedYear"] as String
                allLeagues.append("intFormedYear: $intFormedYear\n")

            }catch(e:Throwable){
                allLeagues.append("intFormedYear: -\n")

            }
            try{
                val strLeague = teams["strLeague"] as String
                allLeagues.append("strLeague: $strLeague\n")

            }catch(e:Throwable){
                allLeagues.append("strLeague: -\n")

            }
            try{
                val idLeague = teams["idLeague"] as String
                allLeagues.append("idLeague: $idLeague\n")

            }catch(e:Throwable){
                allLeagues.append("idLeague: -\n")

            }
            try{
                val strStadium = teams["strStadium"] as String
                allLeagues.append("strStadium: $strStadium\n")

            }catch(e:Throwable){
                allLeagues.append("strStadium: -\n")

            }
            try{
                val strKeywords = teams["strKeywords"] as String
                allLeagues.append("strKeywords: $strKeywords\n")

            }catch(e:Throwable){
                allLeagues.append("strKeywords: -\n")

            }
            try{
                val strStadiumThumb = teams["strStadiumThumb"] as String
                allLeagues.append("strStadiumThumb: $strStadiumThumb\n")

            }catch(e:Throwable){
                allLeagues.append("strStadiumThumb: -\n")

            }
            try{
                val strStadiumLocation = teams["strStadiumLocation"] as String
                allLeagues.append("strStadiumLocation: $strStadiumLocation\n")

            }catch(e:Throwable){
                allLeagues.append("strStadiumLocation: -\n")

            }
            try{
                val intStadiumCapacity = teams["intStadiumCapacity"] as String
                allLeagues.append("intStadiumCapacity: $intStadiumCapacity\n")

            }catch(e:Throwable){
                allLeagues.append("intStadiumCapacity: -\n")

            }
            try{
                val strWebsite = teams["strWebsite"] as String
                allLeagues.append("strWebsite: $strWebsite\n")

            }catch(e:Throwable){
                allLeagues.append("strWebsite: -\n")

            }
            try{
                val strTeamJersey = teams["strTeamJersey"] as String
                allLeagues.append("strTeamJersey: $strTeamJersey\n")

            }catch(e:Throwable){
                allLeagues.append("strTeamJersey: -\n")

            }
            try{
                val strTeamLogo = teams["strTeamLogo"] as String
                allLeagues.append("strTeamLogo: $strTeamLogo\n")

            }catch(e:Throwable){
                allLeagues.append("strTeamLogo: -\n")

            }

        }catch (jen: JSONException){

        }
        allLeagues.append("\n\n")
    }

    return allLeagues.toString()
}

//parseDB uses similar process to above parseJSON, and stores to DB
suspend fun parseDB(stb: StringBuilder) {

    val json = JSONObject(stb.toString())
    var jsonArray: JSONArray = json.getJSONArray("teams")

    for (i in 0 until jsonArray.length()) {
        val teams: JSONObject = jsonArray[i] as JSONObject // this is a json object

        try {
            val idTeamStr = teams["idTeam"] as String
            val idTeamNum = idTeamStr.toInt()

            var strTeam = ""

            try {
                strTeam = teams["strTeam"] as String
            } catch (e: Throwable) {
                strTeam = "-"
            }

            var strAlternate = ""
            try {
                strAlternate = teams["strAlternate"] as String
            } catch (e: Throwable) {
                strAlternate = "-"
            }

            var strTeamShort = ""
            try {
                strTeamShort = teams["strTeamShort"] as String
            } catch (e: Throwable) {
                strTeamShort = "-"
            }

            var intFormedYear = ""
            try {
                intFormedYear = teams["intFormedYear"] as String
            } catch (e: Throwable) {
                intFormedYear = "-"
            }

            var strLeague = ""
            try {
                strLeague = teams["strLeague"] as String
            } catch (e: Throwable) {
                strLeague = "-"
            }

            var idLeague = ""
            try {
                idLeague = teams["idLeague"] as String
            } catch (e: Throwable) {
                idLeague = "-"
            }

            var strStadium = ""
            try {
                strStadium = teams["strStadium"] as String
            } catch (e: Throwable) {
                strStadium = "-"
            }

            var strKeywords = ""
            try {
                strKeywords = teams["strKeywords"] as String
            } catch (e: Throwable) {
                strKeywords = "-"
            }

            var strStadiumThumb = ""
            try {
                strStadiumThumb = teams["strStadiumThumb"] as String
            } catch (e: Throwable) {
                strStadiumThumb = "-"
            }

            var strStadiumLocation = ""
            try {
                strStadiumLocation = teams["strStadiumLocation"] as String
            } catch (e: Throwable) {
                strStadiumLocation = "-"
            }

            var intStadiumCapacity = ""
            try {
                intStadiumCapacity = teams["intStadiumCapacity"] as String
            } catch (e: Throwable) {
                intStadiumCapacity = "-"
            }

            var strWebsite = " "
            try {
                strWebsite = teams["strWebsite"] as String
            } catch (e: Throwable) {
                strWebsite = "-"
            }

            var strTeamJersey = " "
            try {
                strTeamJersey = teams["strTeamJersey"] as String
            } catch (e: Throwable) {
                strTeamJersey = "-"
            }

            var strTeamLogo = " "
            try {
                strTeamLogo = teams["strTeamLogo"] as String
            } catch (e: Throwable) {
                strTeamLogo = "-"
            }

            leagueDao2.insertAllLeagues(
                AllLeague(idTeamNum ,
                    strTeam,
                    strAlternate,
                    strTeamShort,
                    intFormedYear,
                    strLeague,
                    idLeague,
                    strStadium,
                    strKeywords,
                    strStadiumThumb,
                    strStadiumLocation,
                    intStadiumCapacity,
                    strWebsite,
                    strTeamJersey,
                    strTeamLogo)
            )

        } catch (jen: JSONException) {

        }
    }
}
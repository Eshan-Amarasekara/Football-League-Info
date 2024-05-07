package com.example.football_league_info

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import com.example.football_league_info.ui.theme.Football_League_InfoTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

lateinit var db3:LeagueDatabase
lateinit var leagueDao3: LeagueDao

class SearchForClubs : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            db3 = Room.databaseBuilder(
                this,LeagueDatabase::class.java, "Leagues").build()
            leagueDao3 = db3.leagueDao()
            GUIButton3()
        }
    }
}

@Composable
fun GUIButton3() {
    var keyword by rememberSaveable { mutableStateOf("") }
    var clubs by rememberSaveable { mutableStateOf(emptyList<String>()) }
    var logos by rememberSaveable { mutableStateOf(emptyList<String>()) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            TextField(value = keyword, onValueChange = { keyword = it })
        }

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),  horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = {

                scope.launch {
                    clubs= nameGen(keyword)
                    logos= getLogo(keyword)
                }

            },modifier = Modifier
                .padding(top = 8.dp)
                .width(130.dp),
                border = BorderStroke(3.dp, Color(235, 127, 0)),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(Color(235, 127, 0))) {
                Text("Retrieve Clubs")
            }

        }
        Log.e(logos.toString(),"logo")
        Log.e(clubs.toString(),"name")

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
        }
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                for (i in clubs.indices) {
                    Row {
                        if (i < logos.size) {
                            logoGen(logos[i])
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row {
                        Text(text = clubs[i])
                    }

                }
            }
        }
    }
}

suspend fun nameGen(Keyword:String):List<String>{
    val newKeyword=Keyword
    val club= leagueDao3.searchForLeaguesName(newKeyword)
    Log.e(club.toString(),"name.2")

    return club

}
suspend fun getLogo(Keyword:String):List<String>{
    val newKeyword=Keyword
    val logos= leagueDao3.strTeamLogo(newKeyword)
    return logos
}

suspend fun getBitmap(url:String?): ImageBitmap? {
    return try {
        withContext(Dispatchers.IO) {
            val input = URL(url).openStream()
            BitmapFactory.decodeStream(input)?.asImageBitmap()
        }
    } catch (e: Exception){
        null
    }
}

@Composable
fun logoGen(url: String?) {
    var bitmap by rememberSaveable(key = url) { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(url) {
        if (!url.isNullOrEmpty()) {
            bitmap = getBitmap(url)
        }
    }
    val imageModifier = Modifier
        .padding(20.dp)
        .border(BorderStroke(1.dp, Color.Black))
        .size(168.dp)
    bitmap?.let {
        Image(
            painter = BitmapPainter(it),
            contentDescription = null,
            modifier = imageModifier
        )
    }
}



package com.example.appplaymusic

import android.net.LinkAddress
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.appplaymusic.ui.theme.AppPlayMusicTheme
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppPlayMusicTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

data class Album(
    val nameAlbum: String,
    var listMusic: List<Music>
)

data class Music(
    val name: String,
    val music: Int,
    val cover: Int,
)

@Composable
fun MainScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        //horizontalArrangement = Arrangement.Center
    ) {
        ComposeComboBox()
        //pacer(Modifier.height(10.dp))
    }
}

@Composable
fun ComposeComboBox() {
    var expandedAblum by remember {
        mutableStateOf(false)
    }

    var expandedMusic by remember {
        mutableStateOf(false)
    }

    val listAlbum = listOf(
        Album(
            "Album 1",
            listMusic = listOf(
                Music("quanglong1", 1, 1),
                Music("thaivi1", 2, 2),
                Music("sonloc1", 3,4)
            )
        ),
        Album(
            "Album 2",
            listMusic = listOf(
                Music("quanglong2", 1, 1),
                Music("thaivi2", 2, 2),
                Music("sonloc2", 3,4)
            )
        ),
        Album(
            "Album 3",
            listMusic = listOf(
                Music("quanglong3", 1, 1),
                Music("thaivi3", 2, 2),
                Music("sonloc3", 3,4)
            )
        )
    )

    var selectedItem by remember {
        mutableStateOf(listAlbum[0])
    }


    var selectedItemMusic by remember {
        mutableStateOf(listAlbum[0].listMusic[0])
    }

    val icon = if (expandedAblum) {
        Icons.Filled.KeyboardArrowUp
    } else {
        Icons.Filled.KeyboardArrowDown
    }

    val localUriHandler = LocalUriHandler.current

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                //.clickable { expandedAblum = !expandedAblum }
        ) {
            Box{
                Row {
                    Text(
                        text = selectedItem.nameAlbum,
                        fontSize = 30.sp,
                        textAlign = TextAlign.Center,
                        style = TextStyle(fontWeight = FontWeight.Bold)
                    )
                    Icon(icon, contentDescription = "",Modifier.clickable {expandedAblum = !expandedAblum  }.size(40.dp))

                    DropdownMenu(
                        expanded = expandedAblum,
                        onDismissRequest = { expandedAblum = false },
                    ) {
                        listAlbum.forEach {
                            DropdownMenuItem(
                                text = {
                                    Text(text = it.nameAlbum)
                                },
                                onClick = {
                                    selectedItem = it
                                    selectedItemMusic = it.listMusic[0]
                                    expandedAblum = false
                                })
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "MP3 Player", color = Color.Red)

            Spacer(modifier = Modifier.height(20.dp))

            selectedItem.listMusic.forEach {
                Row {
                    Text(text = it.name)
                    Image(painterResource(id =R.drawable.ic_download), contentDescription = "", Modifier.clickable {
                        localUriHandler.openUri("https:\\/\\/c3-ex-swe.nct.vn\\/NhacCuaTui2048\\/HoaiMong-KhaNguyenTrongTai-12652247.mp3?st=Y2P-Ejl493KL1K6hPxvqPg&e=1709041678&t=1708440712270&download=true&fbclid=IwAR1AVbspj-b9KmGk90LJ8zTViSVOujEjko5MblpcoTOh38ohvm8mGRnxqe0")
                    })
                    Image(painterResource(id =R.drawable.ic_play), contentDescription = "")
                }
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppPlayMusicTheme {

    }
}
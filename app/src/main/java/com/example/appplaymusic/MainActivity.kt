package com.example.appplaymusic

import android.content.ContentValues.TAG
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.appplaymusic.ui.theme.AppPlayMusicTheme
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


class MainActivity : ComponentActivity() {
    private lateinit var player: ExoPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppPlayMusicTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    player = ExoPlayer.Builder(this).build()
                    MainScreen(player)
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
    val music: String,
    val cover: Int,
)

val BASE_URL =
    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path + "/musicForMusicApp/"

@Composable
fun MainScreen(player: ExoPlayer) {
    Column(
        modifier = Modifier.fillMaxSize(),
        //horizontalArrangement = Arrangement.Center
    ) {
        ComposeComboBox(player)
    }
}

@Composable
fun ComposeComboBox(player: ExoPlayer) {
    var expandedAblum by remember {
        mutableStateOf(false)
    }

    val listAlbum = listOf(
        Album(
            "Album 1",
            listMusic = listOf(
                Music(
                    "quanglong1",
                    "https://c1-ex-swe.nixcdn.com/NhacCuaTui2043/Aoiharu-Misekai-10611368.mp3?st=CCs8VEZN_T3VDE3SBOzTPA&e=1709344586&download=true",
                    1
                ),
                Music(
                    "thaivi1",
                    "https://c1-ex-swe.nixcdn.com/NhacCuaTui2052/UtaWoOshieteKuretaAnataE-Misekai-13812737.mp3?st=IxQEnq4lFd6y75QRTQxRhA&e=1709347318&download=true",
                    2
                ),
            )
        ),
        Album(
            "Album 2",
            listMusic = listOf(
                Music(
                    "quanglong2",
                    "https://c1-ex-swe.nixcdn.com/NhacCuaTui2048/HoaiMong-KhaNguyenTrongTai-12652247.mp3?st=Y2P-Ejl493KL1K6hPxvqPg&e=1709041678&t=1708440712270",
                    1
                ),
                Music(
                    "thaivi2",
                    "https://c1-ex-swe.nixcdn.com/NhacCuaTui887/FlyersDeathParadeOpening-Bradio-3801236.mp3?st=AIyjbMnIM3DxtRTaxe4b9w&e=1709349740&download=true",
                    2
                ),
            )
        ),
        Album(
            "Album 3",
            listMusic = listOf(
                Music(
                    "quanglong3",
                    "https://c1-ex-swe.nixcdn.com/NhacCuaTui2052/NewWorld-Misekai-13812733.mp3?st=FJ6O74Tr0WApWICdVK7YYA&e=1709347279&download=true",
                    1
                ),
                Music(
                    "thaivi3",
                    "https://c1-ex-swe.nixcdn.com/NhacCuaTui2036/Suzume-RADWIMPSToaka-7990784.mp3?st=Bp_VK-RRiDVstcbfVLr7fA&e=1709349826&download=true",
                    2
                ),
            )
        )
    )

    var selectedItem by remember {
        mutableStateOf(listAlbum[0])
    }

    var selectedItemMusic by remember {
        mutableStateOf(listAlbum[0].listMusic[0])
    }

    var playMusic by remember {
        mutableStateOf(false)
    }

    val icon = if (expandedAblum) {
        Icons.Filled.KeyboardArrowUp
    } else {
        Icons.Filled.KeyboardArrowDown
    }

    LaunchedEffect(key1 = selectedItem ) {
        for (i in 0..selectedItem.listMusic.size - 1) {
            if (File("$BASE_URL${selectedItem.listMusic[i].name}.mp3").exists()) {
                player.addMediaItem(MediaItem.fromUri(Uri.parse("$BASE_URL${selectedItem.listMusic[i].name}.mp3")))
            } else {
                player.addMediaItem(MediaItem.fromUri(""))
            }
        }
    }
    player.prepare();

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(20.dp)
        ) {
            Box {
                Row {
                    Text(
                        text = selectedItem.nameAlbum,
                        fontSize = 30.sp,
                        textAlign = TextAlign.Center,
                        style = TextStyle(fontWeight = FontWeight.Bold)
                    )
                    Icon(
                        icon,
                        contentDescription = "",
                        Modifier
                            .clickable { expandedAblum = !expandedAblum }
                            .size(40.dp)
                    )

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
            selectedItem.listMusic.forEachIndexed {index, it ->
                Row {
                    Text(text = it.name)
                    Image(painterResource(id = R.drawable.ic_download),
                        contentDescription = "",
                        Modifier.clickable {
                            val downloadService = Retrofit.Builder()
                                .baseUrl("https://c1-ex-swe.nixcdn.com/NhacCuaTui2048/")
                                .build().create(Download_Service::class.java)

                            val call: Call<ResponseBody> =
                                downloadService.downloadFileWithFixedUrl(it.music)

                            call.enqueue(object : Callback<ResponseBody> {
                                override fun onResponse(
                                    call: Call<ResponseBody?>?,
                                    response: Response<ResponseBody?>
                                ) {
                                    if (response.isSuccessful) {
                                        Log.d(TAG, "server contacted and has file")
                                        val writtenToDisk: Boolean =
                                            response.body()
                                                ?.let { it1 ->
                                                    writeResponseBodyToDisk(
                                                        it1,
                                                        it.name
                                                    )
                                                } == true
                                        Log.d(TAG, "file download was a success? $writtenToDisk")
                                    } else {
                                        Log.d(TAG, "server contact failed")
                                    }
                                }

                                override fun onFailure(call: Call<ResponseBody?>?, t: Throwable?) {
                                    Log.e(TAG, "error")
                                }
                            })
                        }
                    )

                    if (File("$BASE_URL${it.name}.mp3").exists()) {
                        Image(painterResource(id = R.drawable.ic_play),
                            contentDescription = "",
                            Modifier.clickable {
                                playMusic = !playMusic
                                if (playMusic) {
                                    player.clearMediaItems();
                                    player.addMediaItem(MediaItem.fromUri(Uri.parse("$BASE_URL${it.name}.mp3")))
                                    player.seekTo(0, 0)
                                    player.play()
                                } else {
                                    player.pause()
                                }
                            }
                        )
                    }
                }
            }
        }

    }
}

private fun writeResponseBodyToDisk(body: ResponseBody, fileName: String): Boolean {
    return try {
        // todo change the file location/name according to your needs
        if (!File(BASE_URL).exists()) {
            File(BASE_URL).mkdir()
        }

        val futureStudioIconFile: File =
            File("$BASE_URL$fileName.mp3")

        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null
        try {
            val fileReader = ByteArray(4096)
            val fileSize = body.contentLength()
            var fileSizeDownloaded: Long = 0
            inputStream = body.byteStream()
            outputStream = FileOutputStream(futureStudioIconFile)
            while (true) {
                val read = inputStream.read(fileReader)
                if (read == -1) {
                    break
                }
                outputStream.write(fileReader, 0, read)
                fileSizeDownloaded += read.toLong()
                Log.d(TAG, "file download: $fileSizeDownloaded of $fileSize")
            }
            outputStream.flush()
            true
        } catch (e: IOException) {
            false
        } finally {
            inputStream?.close()
            outputStream?.close()
        }

    } catch (e: IOException) {
        false
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppPlayMusicTheme {
    }
}
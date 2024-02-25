package com.vi.musicplayer

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.media.browse.MediaBrowser
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.fontscaling.MathUtils.lerp
import androidx.compose.ui.unit.sp
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.vi.musicplayer.ui.theme.MusicPlayerTheme
import kotlinx.coroutines.delay
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
import kotlin.math.absoluteValue

class MainActivity : ComponentActivity() {
    private lateinit var player: ExoPlayer

    @SuppressLint("RestrictedApi")
    @OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        player = ExoPlayer.Builder(this).build()
        setContent {
            MusicPlayerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val uiController = rememberSystemUiController()

                    val colors = listOf(
                        Color(0xffff5a5a),
                        Color(0xFF43CE3A),
                        Color(0xFFC8D157),
                        Color(0xFFCA6DEB),
                        Color(0xFFECA5A5),
                        Color(0xFF627DE0),
                    )

                    val darkColors = listOf(
                        Color(0xFF7E2A2A),
                        Color(0xFF2F8F28),
                        Color(0xFF777C34),
                        Color(0xFF541969),
                        Color(0xFF9B3B3B),
                        Color(0xFF35488D),
                    )

                    val colorIndex = remember {
                        mutableIntStateOf(0)
                    }
                    LaunchedEffect(Unit) {
                        colorIndex.intValue += 1;
                    }
                    LaunchedEffect(colorIndex.intValue) {
                        delay(2100);
                        if (colorIndex.intValue < darkColors.lastIndex) {
                            colorIndex.intValue += 1;
                        } else {
                            colorIndex.intValue = 0;
                        }
                    }
                    val animatedColor by animateColorAsState(
                        targetValue = colors[colorIndex.intValue],
                        animationSpec = tween(2000), label = ""
                    )

                    val animatedDarkColor by animateColorAsState(
                        targetValue = darkColors[colorIndex.intValue],
                        animationSpec = tween(2000), label = ""
                    )

                    uiController.setStatusBarColor(animatedColor, darkIcons = false)
                    uiController.setNavigationBarColor(animatedColor)

                    val musics1 = listOf(
                        Music(
                            name = "Aoiharu",
                            fileName = "aoiharu.mp3",
                            cover = R.drawable.cv1,
                            url = "https://c1-ex-swe.nixcdn.com/NhacCuaTui2043/Aoiharu-Misekai-10611368.mp3?st=CCs8VEZN_T3VDE3SBOzTPA&e=1709344586&download=true"
                        ),
                        Music(
                            name = "New world",
                            fileName = "new_world.mp3",
                            cover = R.drawable.cv2,
                            url = "https://c1-ex-swe.nixcdn.com/NhacCuaTui2052/NewWorld-Misekai-13812733.mp3?st=FJ6O74Tr0WApWICdVK7YYA&e=1709347279&download=true"
                        ),
                        Music(
                            name = "Uta Wo Oshiete Kureta Anata E",
                            fileName = "uta_wo_oshiete_kureta_anata_e.mp3",
                            cover = R.drawable.cv3,
                            url = "https://c1-ex-swe.nixcdn.com/NhacCuaTui2052/UtaWoOshieteKuretaAnataE-Misekai-13812737.mp3?st=IxQEnq4lFd6y75QRTQxRhA&e=1709347318&download=true"
                        ),
                    )
                    val musics2 = listOf(
                        Music(
                            name = "Flyers",
                            fileName = "flyers.mp3",
                            cover = R.drawable.cv4,
                            url = "https://c1-ex-swe.nixcdn.com/NhacCuaTui887/FlyersDeathParadeOpening-Bradio-3801236.mp3?st=AIyjbMnIM3DxtRTaxe4b9w&e=1709349740&download=true"
                        ),
                        Music(
                            name = "Suzume",
                            fileName = "suzume.mp3",
                            cover = R.drawable.cv5,
                            url = "https://c1-ex-swe.nixcdn.com/NhacCuaTui2036/Suzume-RADWIMPSToaka-7990784.mp3?st=Bp_VK-RRiDVstcbfVLr7fA&e=1709349826&download=true"
                        ),
                        Music(
                            name = "King",
                            fileName = "king.mp3",
                            cover = R.drawable.cv6,
                            url = "https://c1-ex-swe.nixcdn.com/NhacCuaTui2024/KingCover-Kanaria-7583484.mp3?st=oTCTD-Xn6FEPrZmxdcL6IQ&e=1709350031&download=true"
                        ),
                    )

                    val albums = listOf(musics1, musics2);

                    val selectedAlbumIndex = remember {
                        mutableIntStateOf(0)
                    }
                    val pagerState =
                        rememberPagerState(pageCount = { albums[selectedAlbumIndex.intValue].count() })
                    val playingIndex = remember {
                        mutableIntStateOf(0)
                    }

                    LaunchedEffect(pagerState.currentPage) {
                        playingIndex.intValue = pagerState.currentPage
                        player.seekTo(pagerState.currentPage, 0)
                    }

                    LaunchedEffect(Unit, selectedAlbumIndex.intValue) {
                        albums[selectedAlbumIndex.intValue].forEach {
                            val path = BASE_PATH + it.fileName
                            if (File(path).exists()) {
                                val mediaItem = MediaItem.fromUri(Uri.parse(path))
                                player.addMediaItem(mediaItem)
                            } else {
                                player.addMediaItem(MediaItem.fromUri(""))
                            }
                        }
                    }

                    player.prepare()

                    val playing = remember {
                        mutableStateOf(false)
                    }
                    val currentPosition = remember {
                        mutableLongStateOf(0)
                    }
                    val totalDuration = remember {
                        mutableLongStateOf(0)
                    }
                    val progressSize = remember {
                        mutableStateOf(IntSize(0, 0))
                    }
                    LaunchedEffect(player.isPlaying) {
                        playing.value = player.isPlaying
                    }
                    LaunchedEffect(player.currentPosition) {
                        currentPosition.longValue = player.currentPosition
                    }
                    LaunchedEffect(player.duration) {
                        if (player.duration > 0) {
                            totalDuration.longValue = player.duration
                        }
                    }
                    LaunchedEffect(player.currentMediaItemIndex) {
                        playingIndex.intValue = player.currentMediaItemIndex
                        pagerState.animateScrollToPage(
                            playingIndex.intValue,
                            animationSpec = tween(500)
                        )
                    }
                    var percentReached =
                        currentPosition.longValue.toFloat() / (if (totalDuration.longValue > 0) totalDuration.longValue else 0).toFloat()
                    if (percentReached.isNaN()) {
                        percentReached = 0f
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        animatedColor,
                                        animatedDarkColor
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        val configuration = LocalConfiguration.current
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            var mSelectedText by remember { mutableStateOf("Album 1") }
                            var mExpanded by remember { mutableStateOf(false) }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                ExposedDropdownMenuBox(
                                    expanded = mExpanded,
                                    onExpandedChange = {
                                        mExpanded = !mExpanded
                                    }
                                ) {
                                    TextField(
                                        readOnly = true,
                                        value = mSelectedText,
                                        onValueChange = { },
                                        label = { Text("Label") },
                                        trailingIcon = {
                                            ExposedDropdownMenuDefaults.TrailingIcon(
                                                expanded = mExpanded
                                            )
                                        },
                                        modifier = Modifier.menuAnchor(),
                                        colors = ExposedDropdownMenuDefaults.textFieldColors()
                                    )
                                    ExposedDropdownMenu(
                                        expanded = mExpanded,
                                        onDismissRequest = {
                                            mExpanded = false
                                        }
                                    ) {
                                        DropdownMenuItem(
                                            text = { Text(text = "Album 1") },
                                            onClick = {
                                                mSelectedText = "Album 1"
                                                mExpanded = false
                                                selectedAlbumIndex.intValue = 0
                                                player.clearMediaItems()
                                            })
                                        DropdownMenuItem(
                                            text = { Text(text = "Album 2") },
                                            onClick = {
                                                mSelectedText = "Album 2"
                                                mExpanded = false
                                                selectedAlbumIndex.intValue = 1
                                                player.clearMediaItems()
                                            })
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(80.dp))
                            val textColor by animateColorAsState(
                                targetValue = if (animatedColor.luminance() > .5f) Color(
                                    0xff414141
                                ) else Color.White, label = "",
                                animationSpec = tween(2000)
                            )
                            AnimatedContent(
                                targetState = playingIndex.intValue,
                                label = "",
                                transitionSpec = {
                                    (scaleIn() + fadeIn() togetherWith (scaleOut() + fadeOut()))
                                }) {
                                Text(
                                    text = albums[selectedAlbumIndex.intValue][it].name,
                                    maxLines = 1,
                                    lineHeight = (52 * 1.5).sp,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = 52.sp,
                                    color = textColor,
                                )
                            }
                            Spacer(modifier = Modifier.height(32.dp))
                            HorizontalPager(
                                modifier = Modifier.fillMaxWidth(),
                                state = pagerState,
                                pageSize = PageSize.Fixed((configuration.screenWidthDp / 1.7).dp),
                                contentPadding = PaddingValues(horizontal = 85.dp)
                            ) { page ->
                                Card(
                                    modifier = Modifier
                                        .size((configuration.screenWidthDp / 1.7).dp)
                                        .graphicsLayer {
                                            val pageOffset = (
                                                    (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                                                    ).absoluteValue
                                            val alphaLerp = lerp(
                                                start = 0.04f,
                                                stop = 1f,
                                                amount = 1f - pageOffset.coerceIn(0f, 1f)
                                            )
                                            val scaleLerp = lerp(
                                                start = 0.05f,
                                                stop = 1f,
                                                amount = 1f - pageOffset.coerceIn(0f, .5f)
                                            )
                                            alpha = alphaLerp
                                            scaleX = scaleLerp
                                            scaleY = scaleLerp
                                        }
                                        .border(3.dp, Color.White, CircleShape)
                                        .padding(6.dp),
                                    shape = CircleShape
                                ) {
                                    Image(
                                        painter = painterResource(id = albums[selectedAlbumIndex.intValue][page].cover),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(54.dp))
                            Row(
                                modifier = Modifier.padding(horizontal = 24.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = convertLongToText(currentPosition.longValue),
                                    modifier = Modifier.width(55.dp),
                                    color = textColor,
                                    textAlign = TextAlign.Center
                                )
                                // Progress Box
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                        .height(8.dp)
                                        .padding(horizontal = 8.dp)
                                        .clip(CircleShape)
                                        .background(Color.White)
                                        .onGloballyPositioned {
                                            progressSize.value = it.size
                                        }
                                        .pointerInput(Unit) {
                                            detectTapGestures {
                                                val xPos = it.x
                                                val whereIClicked =
                                                    (xPos.toLong() * totalDuration.longValue) / progressSize.value.width.toLong()
                                                player.seekTo(whereIClicked)
                                            }
                                        },
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    // Status box
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(fraction = if (playing.value) percentReached else 0f)
                                            .fillMaxSize()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color(0xff414141))
                                    )
                                }
                                Text(
                                    text = convertLongToText(totalDuration.longValue),
                                    modifier = Modifier.width(55.dp),
                                    color = textColor,
                                    textAlign = TextAlign.Center
                                )
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 32.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                if (File(BASE_PATH + albums[selectedAlbumIndex.intValue][playingIndex.intValue].fileName).exists()) {
                                    Control(
                                        icon = R.drawable.ic_fast_rewind,
                                        size = 60.dp,
                                        onClick = {
                                            player.seekToPrevious()
                                        })
                                    Control(
                                        icon = if (playing.value) R.drawable.ic_pause else R.drawable.ic_play_arrow,
                                        size = 80.dp,
                                        onClick = {
                                            if (playing.value) {
                                                player.pause()
                                            } else {
                                                player.play()
                                            }
                                        })
                                    Control(
                                        icon = R.drawable.ic_fast_forward,
                                        size = 60.dp,
                                        onClick = {
                                            player.seekToNext()
                                        })
                                } else {
                                    Control(
                                        icon = R.drawable.ic_download,
                                        size = 80.dp,
                                        onClick = {
                                            val downloadService =
                                                Retrofit.Builder()
                                                    .baseUrl("https://c3-ex-swe.nct.vn/NhacCuaTui2048/")
                                                    .build().create(FileDownloadService::class.java)

                                            val call: Call<ResponseBody> =
                                                downloadService.downloadFileWithDynamicUrlSync(
                                                    albums[selectedAlbumIndex.intValue][playingIndex.intValue].url
                                                )

                                            call.enqueue(object : Callback<ResponseBody> {
                                                override fun onResponse(
                                                    call: Call<ResponseBody>,
                                                    response: Response<ResponseBody>
                                                ) {
                                                    if (response.isSuccessful) {
                                                        Log.d(TAG, "server contacted and has file")
                                                        val writtenToDisk =
                                                            writeResponseBodyToDisk(
                                                                response.body()!!,
                                                                albums[selectedAlbumIndex.intValue][playingIndex.intValue].fileName
                                                            )
                                                        Log.d(
                                                            TAG,
                                                            "file download was a success? $writtenToDisk"
                                                        )
                                                        val path =
                                                            BASE_PATH + albums[selectedAlbumIndex.intValue][playingIndex.intValue].fileName
                                                        val mediaItem =
                                                            MediaItem.fromUri(Uri.parse(path))
                                                        player.removeMediaItem(playingIndex.intValue)
                                                        player.addMediaItem(playingIndex.intValue, mediaItem)
                                                        player.seekTo(playingIndex.intValue, 0)
                                                        player.play()
                                                        Toast.makeText(applicationContext, "File downloaded", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Log.d(TAG, "server contact failed")
                                                    }
                                                }

                                                override fun onFailure(
                                                    call: Call<ResponseBody>,
                                                    t: Throwable
                                                ) {
                                                    Log.e(TAG, "error")
                                                }
                                            })
                                        })
                                }
                            }

                        }
                    }
                }
            }
        }
    }
}

private fun writeResponseBodyToDisk(body: ResponseBody, fileName: String): Boolean {
    return try {
        // Create MusicApp dir if not exits
        if (!File(BASE_PATH).exists()) {
            File(BASE_PATH).mkdir()
        }

        val file =
            File(BASE_PATH + fileName)
        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null
        try {
            val fileReader = ByteArray(4096)
            val fileSize: Long = body.contentLength()
            var fileSizeDownloaded: Long = 0
            inputStream = body.byteStream()
            outputStream =
                FileOutputStream(file)
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


fun convertLongToText(long: Long): String {
    val sec = long / 1000
    val minutes = sec / 60
    val seconds = sec % 60

    val minutesString = if (minutes < 10) {
        "0${minutes}"
    } else {
        minutes.toString()
    }
    val secondString = if (seconds < 10) {
        "0${seconds}"
    } else {
        seconds.toString()
    }
    return "$minutesString:$secondString"
}

@Composable
fun Control(icon: Int, size: Dp, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(Color.White)
            .clickable { onClick() }, contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = "",
            modifier = Modifier.size(size / 2),
            tint = Color(0xff414141),
        )
    }
}

data class Music(
    val name: String,
    val cover: Int,
    val fileName: String,
    val url: String,
)
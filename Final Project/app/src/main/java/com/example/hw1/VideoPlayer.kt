package com.example.hw1

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults.Medium
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@Composable
fun VideoPlayer (onNavigateToAllNotes: () -> Unit) {

    val productSansRegular = FontFamily(Font(R.font.productsansregular))
    val context = LocalContext.current

    // Video picker from gallery
    var videoUri by remember { mutableStateOf<Uri?>(null) }

    val videoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri: Uri? ->
            if (uri != null) {
                val newUri = copyingVideo(context, uri)
                videoUri = newUri
            }
        },
    )

    Surface {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .shadow(20.dp)
                    .background(MaterialTheme.colorScheme.primary),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = { onNavigateToAllNotes() }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onPrimary,
                    )
                }

                Text(
                    text = "Video Player",
                    style = TextStyle(
                        fontSize = 27.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = productSansRegular,
                        letterSpacing = 1.5.sp
                    ),
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .weight(1f) // Adjust the weight to distribute space
                )
            }

            Button(
                onClick = {
                    videoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly))
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 5.dp,
                    pressedElevation = 0.dp
                ),
                modifier = Modifier
                    .padding(all = 20.dp)
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                Text(
                    text = "Select a video",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = productSansRegular,
                        letterSpacing = 1.sp
                    )
                )
            }

            if(videoUri != null) {
                Divider(
                    color = MaterialTheme.colorScheme.onSurface,
                    thickness = 1.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp)
                )

                Box(
                    modifier = Modifier
                        .padding(all = 20.dp)
                        .fillMaxSize()
                ) {
                    ExoPlayerView(videoUri!!)
                }
            }
        }
    }
}

@Composable
fun ExoPlayerView(videoUri: Uri) {

    val context = LocalContext.current

    // Initialize ExoPlayer
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build()
    }

    val mediaSource = remember(videoUri) {
        MediaItem.fromUri(videoUri)
    }

    // Set MediaSource to ExoPlayer
    LaunchedEffect(exoPlayer, mediaSource) {
        exoPlayer.setMediaItem(mediaSource)
        exoPlayer.prepare()
    }

    // Manage lifecycle events
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    AndroidView(
        factory = { cntx ->
            PlayerView(cntx).apply {
                player = exoPlayer
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .clip(Medium)
    )
}

fun copyingVideo(context: Context, newUri: Uri): Uri {
    context.contentResolver.openInputStream(newUri).use { stream ->
        val outputFile =
            context.filesDir.resolve("video_" + System.currentTimeMillis() + ".mp4")
        stream!!.copyTo(outputFile.outputStream())
        return outputFile.toUri()
    }
}

@Preview
@Composable
fun VideoPlayerPreview() {
    VideoPlayer(onNavigateToAllNotes = {})
}
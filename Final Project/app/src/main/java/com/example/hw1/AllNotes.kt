package com.example.hw1

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Divider
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.hw1.ui.theme.HW1Theme

@Composable
fun AllNotes(
    onNavigateToNewNote: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToVideoPlayer: () -> Unit
) {

    val productSansRegular = FontFamily(Font(R.font.productsansregular))
    val context = LocalContext.current
    val db = AppDatabase.getInstance(context)
    var dbNotes by remember {
        mutableStateOf(db.noteDao().getAllNotes())
    }

    Scaffold (
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .shadow(20.dp)
                    .background(MaterialTheme.colorScheme.primary),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Notes",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.surface,
                        fontSize = 27.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = productSansRegular,
                        letterSpacing = 1.5.sp
                    ),
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .weight(1f) // Adjust the weight to distribute space
                )

                IconButton(
                    onClick = { onNavigateToVideoPlayer() },
                    modifier = Modifier.padding(end = 20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.VideoLibrary,
                        contentDescription = "Video Player",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(26.dp)
                    )
                }

                IconButton(
                    onClick = { onNavigateToSettings() },
                    modifier = Modifier.padding(end = 20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "Settings",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

        },
        modifier = Modifier.fillMaxHeight(),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = {
                    Text(
                        text = "Add Note",
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = productSansRegular,
                            letterSpacing = 1.5.sp
                        )
                    )
                },
                onClick = { onNavigateToNewNote() },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "New Note",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            )
        }
    ) { paddingValues ->

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {

            val profilePictureUri: String = db.userDao().getProPicUri(0)
            val username = db.userDao().getAll()[0].username
            val welcomeText = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Normal)) {
                    append("Hello, ")
                }
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(username)
                }
                append("!")
            }
            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
            ) {
                Text(
                    text = welcomeText,
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 30.sp,
                        fontFamily = productSansRegular,
                        letterSpacing = 1.5.sp
                    ),
                    modifier = Modifier
                        .padding(start = 20.dp, top = 20.dp, bottom = 10.dp)
                        .align(Alignment.CenterVertically)
                        .weight(1f)
                )

                Image(
                    painter = rememberAsyncImagePainter(model = profilePictureUri),
                    contentDescription = "Profile picture",
                    modifier = Modifier
                        .padding(end = 20.dp, top = 10.dp, bottom = 10.dp)
                        .size(80.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
                )
            }


            Divider(
                color = MaterialTheme.colorScheme.onSurface,
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, bottom = 30.dp)
            )

            LazyColumn {
                items(dbNotes) { note ->
                    NoteCard(
                        note,
                        updateNotes = {
                            dbNotes = dbNotes.filter { it != note }
                            Toast.makeText(context, "Note deleted", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}


@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun PreviewAllNotes() {
    HW1Theme {
        AllNotes(onNavigateToNewNote = {}, onNavigateToSettings = {}, onNavigateToVideoPlayer = {})
    }
}
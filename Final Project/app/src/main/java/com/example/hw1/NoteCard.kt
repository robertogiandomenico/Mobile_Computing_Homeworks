package com.example.hw1

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter

@Composable
fun NoteCard(note: Note, updateNotes: () -> Unit) {

    val productSansRegular = FontFamily(Font(R.font.productsansregular))
    val db = AppDatabase.getInstance(LocalContext.current)

    val profilePictureUri: String = db.userDao().getProPicUri(0)

    // Add padding around our message
    Row(modifier = Modifier.padding(all = 8.dp)) {
        Image(
            painter = rememberAsyncImagePainter(model = profilePictureUri),
            contentDescription = "Profile picture",
            modifier = Modifier
                // Set image size to 40 dp
                .size(40.dp)
                // Clip image to be shaped as a circle
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )

        // Add a horizontal space between the image and the column
        Spacer(modifier = Modifier.width(8.dp))

        // We keep track if the message is expanded or not in this variable
        var isExpanded by remember { mutableStateOf(false) }
        // surfaceColor will be updated gradually from one color to the other
        val surfaceColor by animateColorAsState(
            if (isExpanded) {
                MaterialTheme.colorScheme.tertiaryContainer
            }
            else {
                MaterialTheme.colorScheme.primaryContainer
            },
        )

        // We toggle the isExpanded variable when we click on this Column
        Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
            Text(
                text = note.title,
                color = MaterialTheme.colorScheme.secondary,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontFamily = productSansRegular,
                    letterSpacing = 1.sp,
                    fontSize = 15.sp
                )
            )
            // Add a vertical space between the author and message texts
            Spacer(modifier = Modifier.height(4.dp))

            Box(
                modifier = Modifier
                    .padding(start = 5.dp, end = 20.dp, bottom = 5.dp)
                    .shadow(5.dp, shape = MaterialTheme.shapes.medium, true)
                    .fillMaxWidth()
                    .animateContentSize()
                    .background(surfaceColor, shape = MaterialTheme.shapes.medium)

            ) {
                Column {
                    if (!isExpanded)
                        Text(
                            text = note.body,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = 15.dp),
                            // If the message is expanded, we display all its content
                            // otherwise we only display the first line
                            maxLines = 1,
                            style = TextStyle(
                                fontFamily = productSansRegular,
                                letterSpacing = 1.sp,
                                fontSize = 15.sp
                            )
                        )

                    if (isExpanded) {
                        Text(
                            text = note.body,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 15.dp, top = 15.dp, end = 15.dp),
                            style = TextStyle(
                                fontFamily = productSansRegular,
                                letterSpacing = 1.sp,
                                fontSize = 15.sp
                            )
                        )

                        IconButton(
                            onClick = {
                                db.noteDao().delete(note)
                                updateNotes()
                            },
                            modifier = Modifier
                                .align(Alignment.End)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Delete note",
                                tint = MaterialTheme.colorScheme.secondary,
                            )
                        }
                    }
                }
            }

        }
    }
}
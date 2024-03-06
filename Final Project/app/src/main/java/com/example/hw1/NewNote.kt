package com.example.hw1

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hw1.ui.theme.HW1Theme

// New Message Screen
@Composable
fun NewNote(onNavigateToAllNotes: () -> Unit) {

    val db = AppDatabase.getInstance(LocalContext.current)
    val productSansRegular = FontFamily(Font(R.font.productsansregular))
    var noteTitle by remember { mutableStateOf("") }
    var noteBody by remember { mutableStateOf("") }
    val context = LocalContext.current

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
                    text = "New Note",
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
            }

            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                value = noteTitle,
                onValueChange = { newNoteTitle -> noteTitle = newNoteTitle },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                label = { Text(text = "Title") },
                maxLines = 1,
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp,
                    fontFamily = productSansRegular,
                    letterSpacing = 1.sp,
                )
            )

            TextField(
                value = noteBody,
                onValueChange = { newNoteBody -> noteBody = newNoteBody },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text(text = "Enter note") },
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp,
                    fontFamily = productSansRegular,
                    letterSpacing = 1.sp
                )
            )

            // Add a vertical space between the text and the button
            Spacer(modifier = Modifier.height(10.dp))

            // Add Message Button
            Button(
                onClick = {
                    if(noteTitle.isEmpty() || noteBody.isEmpty()) {
                        Toast.makeText(context, "Please fill all the fields!", Toast.LENGTH_LONG).show()
                        return@Button
                    }

                    val newNote = Note(
                        title = noteTitle,
                        body = noteBody
                    )
                    db.noteDao().insertAll(newNote)
                    Toast.makeText(context, "Note added!", Toast.LENGTH_LONG).show()
                    onNavigateToAllNotes()
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
                    .height(70.dp)
            ) {
                Text(
                    text = "Add Note",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.surface,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = productSansRegular,
                        letterSpacing = 1.sp
                    )
                )
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
fun PreviewNewNote() {
    HW1Theme {
        NewNote(onNavigateToAllNotes = {})
    }
}
package com.example.hw1

import android.content.Context
import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import com.example.hw1.ui.theme.HW1Theme


@Composable
fun Settings(onNavigateToConversation: () -> Unit) {

    val context = LocalContext.current
    val db = AppDatabase.getInstance(context)

    var imageUri by remember { mutableStateOf<Uri?>(db.userDao().getProPicUri(0).toUri()) }

    val photoPicker = rememberLauncherForActivityResult(
        contract =  ActivityResultContracts.PickVisualMedia(),
        onResult = { uri: Uri? ->
            if (uri != null) {
                val newUri = copying(context, uri)
                db.userDao().setProPicUri(0, newUri.toString())
                imageUri = newUri
            }
        },
    )

    Surface {
        Column {
            Row {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary,
                    shadowElevation = 10.dp,
                ) {

                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(start = 20.dp, top = 10.dp, bottom = 10.dp)
                    )

                    IconButton(
                        onClick = { onNavigateToConversation() }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.surface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Row(
                modifier = Modifier.padding(start = 20.dp, top = 10.dp, end = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Image (
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = "Contact profile picture",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
                )

                Button(
                    onClick = {
                        photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = MaterialTheme.colorScheme.surface,
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 5.dp,
                        pressedElevation = 0.dp
                    ),
                    modifier = Modifier
                        .padding(all = 20.dp)
                        .width(250.dp)
                        .height(40.dp)
                ) {
                    Text(
                        text = "Change profile picture",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            Divider(
                color = MaterialTheme.colorScheme.onSurface,
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 20.dp)
            )

            Column {

                var text by remember { mutableStateOf(db.userDao().getAll()[0].username.toString()) }
                TextField(
                    value = text,
                    onValueChange = {
                        newText -> text = newText
                        db.userDao().setUsername(0, text)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp),
                    textStyle = MaterialTheme.typography.bodyMedium,
                    label = { Text("Name") }
                )
            }

        }
    }
}

fun copying(context: Context, newUri: Uri): Uri {
    context.contentResolver.openInputStream(newUri).use { stream ->
        val outputFile = context.filesDir.resolve("profile_picture.jpg" + System.currentTimeMillis())
        stream!!.copyTo(outputFile.outputStream())
        return outputFile.toUri()
    }
}

@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun PreviewSettings() {
    HW1Theme {
        Settings(onNavigateToConversation = {})
    }
}

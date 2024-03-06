package com.example.hw1

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
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
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import android.content.pm.PackageManager
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import com.google.accompanist.permissions.PermissionState
import com.example.hw1.ui.theme.HW1Theme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState


@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun Settings(onNavigateToAllNotes: () -> Unit) {

    val context = LocalContext.current
    val db = AppDatabase.getInstance(context)
    val productSansRegular = FontFamily(Font(R.font.productsansregular))

    // Photo picker from gallery
    var imageUri by remember { mutableStateOf<Uri?>(db.userDao().getProPicUri(0).toUri()) }
    var showProfilePictureMenu by remember { mutableStateOf(false) }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri: Uri? ->
            if (uri != null) {
                val newUri = copying(context, uri)
                db.userDao().setProPicUri(0, newUri.toString())
                imageUri = newUri
            }
        },
    )

    // Camera stuff
    val cameraPermissionState: PermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    var takePicture by remember { mutableStateOf(false) }

    if (takePicture) {
        CameraContent(
            hasCameraPermission = cameraPermissionState.status.isGranted,
            onRequestPermission = {
                cameraPermissionState.launchPermissionRequest()
            }
        )
    }


    createNotificationChannel(context)
    val channelId = "all_notifications" // Use same Channel ID
    val intent = Intent(context, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    val POST_NOTIFICATIONS_PERMISSION_CODE = 123 // Use any unique code

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
                    text = "Settings",
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


            Spacer(modifier = Modifier.width(10.dp))

            Row(
                modifier = Modifier.padding(start = 20.dp, top = 10.dp, end = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = "Profile picture",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
                )

                Button(
                    onClick = {
                        showProfilePictureMenu = !showProfilePictureMenu
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
                        .width(250.dp)
                        .height(40.dp)
                ) {
                    Text(
                        text = "Change profile picture",
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = productSansRegular,
                            letterSpacing = 1.sp
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


            var newName by remember {
                mutableStateOf(
                    db.userDao().getAll()[0].username.toString()
                )
            }

            TextField(
                value = newName,
                onValueChange = { newText ->
                    newName = newText
                    db.userDao().setUsername(0, newName)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp),
                label = { Text("Name") },
                maxLines = 1,
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp,
                    fontFamily = productSansRegular,
                    letterSpacing = 1.sp
                )
            )

            Divider(
                color = MaterialTheme.colorScheme.onSurface,
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, top = 20.dp, end = 20.dp)
            )

            Button(
                onClick = {
                    // Create the notification
                    val builder = NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContentTitle("Hello World")
                        .setContentText("This is a test notification")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                    builder.setContentIntent(pendingIntent).setAutoCancel(true)

                    // Display the notification
                    with(NotificationManagerCompat.from(context)) {
                        if (ActivityCompat.checkSelfPermission(
                                context,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            ActivityCompat.requestPermissions(
                                context as MainActivity,
                                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                                POST_NOTIFICATIONS_PERMISSION_CODE
                            )
                        }
                        notify(123, builder.build())
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.surface,
                    containerColor = MaterialTheme.colorScheme.secondary
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 5.dp,
                    pressedElevation = 0.dp
                ),
                modifier = Modifier
                    .padding(all = 20.dp)
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Text(
                    text = "Trigger Notifications",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.surface,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = productSansRegular,
                        letterSpacing = 1.sp
                    )
                )
            }
        }

        if (showProfilePictureMenu) {
            ProfilePictureMenu(
                onPickFromStorage = {
                    // Close menu and open gallery
                    showProfilePictureMenu = false // close menu
                    photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
                onTakePicture = {
                    // Close menu and open camera
                    showProfilePictureMenu = false
                    takePicture = true
                }
            )

            takePicture = false
        }
    }
}

@Composable
fun CameraContent(
    hasCameraPermission: Boolean,
    onRequestPermission: () -> Unit
) {
    if (hasCameraPermission) {
        CameraScreen()
    } else {
        NoPermissionDialog(onRequestPermission)
    }
}

fun copying(context: Context, newUri: Uri): Uri {
    context.contentResolver.openInputStream(newUri).use { stream ->
        val outputFile =
            context.filesDir.resolve("profile_picture_" + System.currentTimeMillis() + ".jpg")
        stream!!.copyTo(outputFile.outputStream())
        return outputFile.toUri()
    }
}

fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "Test Notifications"
        val descriptionText = "Your Channel Description"
        val importance = NotificationManager.IMPORTANCE_HIGH // Set importance to high
        val channel = NotificationChannel("Test Notifications", name, importance).apply {
            description = descriptionText
        }
        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun PreviewSettings() {
    HW1Theme {
        Settings(onNavigateToAllNotes = {})
    }
}

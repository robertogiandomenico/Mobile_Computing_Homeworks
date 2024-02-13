package com.example.hw1

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hw1.ui.theme.HW1Theme

// New Message Screen
@Composable
fun NewMessage(onNavigateToConversation: () -> Unit) {
    Surface {
        Column {
            Row {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary,
                    shadowElevation = 10.dp,
                ) {

                    Text(
                        text = "New Message",
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

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(R.drawable.default_avatar),
                    contentDescription = "Default profile picture",
                    modifier = Modifier
                        .padding(start = 20.dp)
                        // Set image size
                        .size(50.dp)
                        // Clip image to be shaped as a circle
                        .clip(CircleShape)
                        .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)

                )

                // Add a horizontal space between the image and the name
                Spacer(modifier = Modifier.width(10.dp))

                var name by remember { mutableStateOf("") }

                TextField(
                    value = name,
                    onValueChange = { newText -> name = newText },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textStyle = MaterialTheme.typography.bodyLarge,
                    placeholder = { Text(text = "Enter contact name") }
                )
            }


            // Add a vertical space between the name and the text field
            Spacer(modifier = Modifier.height(20.dp))

            Surface(
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 1.dp,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    var text by remember { mutableStateOf("") }

                    TextField(
                        value = text,
                        onValueChange = { newText -> text = newText },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        textStyle = MaterialTheme.typography.bodyMedium,
                        placeholder = { Text(text = "Enter message") }
                    )

                    // Add a vertical space between the text and the button
                    Spacer(modifier = Modifier.height(10.dp))

                    // Send Message Button
                    Button(
                        onClick = { },
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
                            .fillMaxWidth()
                            .height(70.dp)
                    ) {
                        Text(
                            text = "Send Message",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
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
fun PreviewNewMessage() {
    HW1Theme {
        NewMessage(onNavigateToConversation = {})
    }
}
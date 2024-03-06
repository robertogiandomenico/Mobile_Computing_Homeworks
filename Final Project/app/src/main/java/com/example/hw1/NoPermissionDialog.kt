package com.example.hw1

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun NoPermissionDialog(onRequestPermission: () -> Unit) {
    val productSansRegular = FontFamily(Font(R.font.productsansregular))

    var showDialog by remember { mutableStateOf(true) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Camera,
                    contentDescription = "Camera Icon"
                )
            },
            title = {
                Text(
                    text = "Camera Permission Required",
                    style = TextStyle(
                        fontFamily = productSansRegular,
                        letterSpacing = 1.sp,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            },
            text = {
                Text(
                    text = "Please grant the permission to use the camera",
                    style = TextStyle(
                        fontFamily = productSansRegular,
                        letterSpacing = 1.sp,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        onRequestPermission()
                    }
                ) {
                    Text(
                        text = "OK",
                        style = TextStyle(
                            fontFamily = productSansRegular,
                            letterSpacing = 1.sp,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                    }
                ) {
                    Text(
                        text = "Dismiss",
                        style = TextStyle(
                            fontFamily = productSansRegular,
                            letterSpacing = 1.sp,
                            fontSize = 15.sp
                        )
                    )
                }
            }
        )
    }
}

@Preview
@Composable
fun NoPermissionScreenPreview() {
    NoPermissionDialog(onRequestPermission = {})
}
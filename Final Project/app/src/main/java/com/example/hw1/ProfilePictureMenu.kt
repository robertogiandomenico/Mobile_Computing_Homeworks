package com.example.hw1

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePictureMenu(onPickFromStorage: () -> Unit, onTakePicture: () -> Unit) {
    val productSansRegular = FontFamily(Font(R.font.productsansregular))
    var showBottomSheet by remember { mutableStateOf(true) }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Button(
                    onClick = {
                        showBottomSheet = false
                        onPickFromStorage()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp, bottom = 8.dp)
                ) {
                    Text(
                        text = "Choose from Storage",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = productSansRegular,
                            letterSpacing = 1.5.sp
                        ),
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .padding(all = 15.dp)
                    )
                }

                Button(
                    onClick = {
                        showBottomSheet = false
                        onTakePicture()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, top = 8.dp, end = 15.dp, bottom = 20.dp)
                ) {
                    Text(
                        text = "Take a picture",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = productSansRegular,
                            letterSpacing = 1.5.sp
                        ),
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .padding(all = 15.dp)
                    )
                }
            }
        }
    }
}
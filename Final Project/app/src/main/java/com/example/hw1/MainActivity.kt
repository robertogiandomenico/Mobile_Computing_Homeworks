package com.example.hw1

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hw1.ui.theme.HW1Theme

class MainActivity : ComponentActivity() {

    private val gyroscopeListener by lazy { GyroscopeListener(this) }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContent {
            HW1Theme {
                Surface {
                    MyApp()
                }
            }
        }

        // Start listening when the activity is created
        gyroscopeListener.startListening()
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MyApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "allNotes"
    ) {
        composable("allNotes") {
            AllNotes(
                onNavigateToNewNote = {
                    navController.navigate("newNote")
                },
                onNavigateToSettings = {
                    navController.navigate("settings")
                },
                onNavigateToVideoPlayer = {
                    navController.navigate("videoPlayer")
                }
            )
        }
        composable("newNote") {
            NewNote(
                onNavigateToAllNotes = {
                    navController.navigate("allNotes") {
                        popUpTo("allNotes") {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable("settings") {
            Settings(
                onNavigateToAllNotes = {
                    navController.navigate("allNotes") {
                        popUpTo("allNotes") {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable("videoPlayer") {
            VideoPlayer(
                onNavigateToAllNotes = {
                    navController.navigate("allNotes") {
                        popUpTo("allNotes") {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}

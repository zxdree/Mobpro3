package com.adre0089.mini_projek3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.adre0089.mini_projek3.ui.screen.MainScreen
import com.adre0089.mini_projek3.ui.theme.Mobpro3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Mobpro3Theme {
                MainScreen()
            }
        }
    }
}




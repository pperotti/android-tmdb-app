package com.pperotti.android.moviescatalogapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.pperotti.android.moviescatalogapp.navigation.SetupNavigation
import com.pperotti.android.moviescatalogapp.ui.theme.MoviesCatalogAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            MoviesCatalogAppTheme {
                SetupNavigation()
            }
        }
    }
}

package com.binit.flightrewards

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.binit.flightrewards.navigation.AppNavigation
import com.binit.flightrewards.ui.theme.FlightRewardsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {

      FlightRewardsTheme {

        Surface(
          color = MaterialTheme.colorScheme.background
        ) {

          AppNavigation()
        }
      }
    }
  }
}
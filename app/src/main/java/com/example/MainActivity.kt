package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.MainAppScreen
import com.example.ui.theme.RepairReachTheme
import com.example.ui.viewmodel.RepairViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      RepairReachTheme {
        val viewModel: RepairViewModel = viewModel()
        MainAppScreen(viewModel = viewModel)
      }
    }
  }
}

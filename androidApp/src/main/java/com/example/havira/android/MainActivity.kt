package com.example.havira.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.havira.android.dish.create.presentation.AndroidCreateDishViewModel
import com.example.havira.android.dish.create.presentation.CreateDishScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    HaviraRoot()
                }
            }
        }
    }
}

@Composable
fun HaviraRoot(){
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.CREATE_DISH
    ) {
        composable(route = Routes.CREATE_DISH) {
            val viewModel = hiltViewModel<AndroidCreateDishViewModel>()
            val state by viewModel.state.collectAsState()

            CreateDishScreen(state = state, onEvent = { event ->
                viewModel.onEvent(event)
            })
        }
    }
}



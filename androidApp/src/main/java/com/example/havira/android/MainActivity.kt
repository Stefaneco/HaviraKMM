package com.example.havira.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.example.havira.android.dish.list.presentation.AndroidDishListViewModel
import com.example.havira.android.dish.list.presentation.DishListScreen
import com.example.havira.dish.presentation.list.DishListEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
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
        startDestination = Routes.DISH_LIST
    ) {
        composable(route = Routes.CREATE_DISH) {
            val viewModel = hiltViewModel<AndroidCreateDishViewModel>()
            val state by viewModel.state.collectAsState()

            CreateDishScreen(state = state, onEvent = { event ->
                viewModel.onEvent(event)
            })
        }
        composable(Routes.DISH_LIST){
            val viewModel = hiltViewModel<AndroidDishListViewModel>()
            val state by viewModel.state.collectAsState()

            DishListScreen(state = state, onEvent = { event ->
                when(event){
                    is DishListEvent.CreateDish -> {
                        navController.navigate(Routes.CREATE_DISH)
                    }
                    is DishListEvent.SelectDish -> {

                    }
                    else -> {
                        viewModel.onEvent(event)
                    }
                }
            })
        }
    }
}



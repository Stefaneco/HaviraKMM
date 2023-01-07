package com.example.havira.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.havira.android.dish.create.presentation.AndroidCreateDishViewModel
import com.example.havira.android.dish.create.presentation.CreateDishScreen
import com.example.havira.android.dish.details.presentation.AndroidDishDetailViewModel
import com.example.havira.android.dish.details.presentation.DishDetailScreen
import com.example.havira.android.dish.list.presentation.AndroidDishListViewModel
import com.example.havira.android.dish.list.presentation.DishListScreen
import com.example.havira.dish.presentation.create.CreateDishEvent
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HaviraRoot(){
    val navController = rememberNavController()
    Scaffold(
        modifier = Modifier.padding(4.dp)
    ) { padding ->
        NavHost(
            modifier = Modifier.padding(padding),
            navController = navController,
            startDestination = Routes.DISH_LIST
        ) {
            composable(route = Routes.CREATE_DISH) {
                val viewModel = hiltViewModel<AndroidCreateDishViewModel>()
                val state by viewModel.state.collectAsState()

                CreateDishScreen(state = state, onEvent = { event ->
                    when(event){
                        is CreateDishEvent.CreateDish -> {
                            viewModel.onEvent(
                                CreateDishEvent.CreateDish{
                                navController.navigate(Routes.DISH_LIST) {
                                    popUpTo(Routes.CREATE_DISH)
                                }
                            })
                        }
                        else -> viewModel.onEvent(event)
                    }

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
                            navController.navigate(Routes.DISH_DETAILS_ARGS.format(event.dishId))
                        }
                        else -> {
                            viewModel.onEvent(event)
                        }
                    }
                })
            }
            composable(Routes.DISH_DETAILS){ backStackEntry ->
                val viewModel = hiltViewModel<AndroidDishDetailViewModel>()
                val state by viewModel.state.collectAsState()
                val dishId = backStackEntry.arguments?.getString("dishId")?.toLong() ?: -1
                viewModel.loadDish(dishId)
                DishDetailScreen(state = state, onEvent = {event ->
                    viewModel.onEvent(event)
                })
            }
        }
    }

}



package com.piotrkalin.havira.android

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.piotrkalin.havira.android.auth.presentation.AndroidLoginViewModel
import com.piotrkalin.havira.android.auth.presentation.LoginScreen
import com.piotrkalin.havira.android.core.presentation.AndroidNavigationDrawerViewModel
import com.piotrkalin.havira.android.dish.presentation.create.AndroidCreateDishViewModel
import com.piotrkalin.havira.android.dish.presentation.create.CreateDishScreen
import com.piotrkalin.havira.android.dish.presentation.details.AndroidDishDetailViewModel
import com.piotrkalin.havira.android.dish.presentation.details.DishDetailScreen
import com.piotrkalin.havira.android.dish.presentation.edit.AndroidDishEditViewModel
import com.piotrkalin.havira.android.dish.presentation.edit.EditDishScreen
import com.piotrkalin.havira.android.dish.presentation.list.AndroidDishListViewModel
import com.piotrkalin.havira.android.dish.presentation.list.DishListScreen
import com.piotrkalin.havira.android.group.create.presentation.AndroidCreateGroupViewModel
import com.piotrkalin.havira.android.group.create.presentation.CreateGroupScreen
import com.piotrkalin.havira.android.group.join.AndroidJoinGroupViewModel
import com.piotrkalin.havira.android.group.join.JoinGroupScreen
import com.piotrkalin.havira.android.groupDish.presentation.create.AndroidCreateGroupDishViewModel
import com.piotrkalin.havira.android.groupDish.presentation.details.AndroidGroupDishDetailViewModel
import com.piotrkalin.havira.android.groupDish.presentation.edit.AndroidEditGroupDishViewModel
import com.piotrkalin.havira.android.groupDish.presentation.list.AndroidGroupDishListViewModel
import com.piotrkalin.havira.android.groupDish.presentation.list.GroupDishListScreen
import com.piotrkalin.havira.auth.presentation.LoginEvent
import com.piotrkalin.havira.core.presentation.NavigationDrawerEvent
import com.piotrkalin.havira.dish.presentation.create.CreateDishEvent
import com.piotrkalin.havira.dish.presentation.detail.DishDetailEvent
import com.piotrkalin.havira.dish.presentation.edit.DishEditEvent
import com.piotrkalin.havira.dish.presentation.list.DishListEvent
import com.piotrkalin.havira.group.presentation.create.CreateGroupEvent
import com.piotrkalin.havira.group.presentation.join.JoinGroupEvent
import com.piotrkalin.havira.groupDish.presentation.list.GroupDishListEvent
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.client.*
import io.ktor.client.engine.cio.*

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

fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("no activity")
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HaviraRoot(){
    val navController = rememberNavController()
    val navDrawerViewModel = hiltViewModel<AndroidNavigationDrawerViewModel>()
    val navDrawerState by navDrawerViewModel.state.collectAsState()

    Scaffold(
        modifier = Modifier.padding(4.dp)
    ) { padding ->
        NavHost(
            modifier = Modifier.padding(padding),
            navController = navController,
            startDestination = Routes.DISH_LIST
        ) {
            composable(route = Routes.GROUP_DISH_DETAILS){
                val viewModel = hiltViewModel<AndroidGroupDishDetailViewModel>()
                val state by viewModel.state.collectAsState()

                DishDetailScreen(state = state, onEvent = { event ->
                    when(event){
                        is DishDetailEvent.BackButtonPressed -> {
                            navController.popBackStack(route = Routes.GROUP, inclusive = false)
                        }
                        is DishDetailEvent.EditButtonPressed -> {
                            navController.navigate(Routes.GROUP_DISH_EDIT_ARGS.format(event.dishId))
                        }
                        else -> {viewModel.onEvent(event)}
                    }
                })
            }

            composable(route = Routes.GROUP_DISH_EDIT){
                val viewModel = hiltViewModel<AndroidEditGroupDishViewModel>()
                val state by viewModel.state.collectAsState()

                EditDishScreen(state = state, onEvent = { event ->
                    when(event){
                        is DishEditEvent.BackButtonPressed -> {
                            navController.popBackStack()
                        }
                        is DishEditEvent.EditDish -> {
                            viewModel.onEvent(
                                DishEditEvent.EditDish {
                                    navController.navigate(Routes.GROUP_DISH_DETAILS_ARGS.format(state.dishId)) {
                                        popUpTo(Routes.GROUP_ARGS.format(state.groupId))
                                    }
                                    //navController.popBackStack()
                                }
                            )
                        }
                        is DishEditEvent.DeleteDish -> {
                            viewModel.onEvent(
                                DishEditEvent.DeleteDish {
                                    navController.popBackStack(
                                        route = Routes.GROUP.format(state.groupId),
                                        inclusive = false
                                    )
                                }
                            )
                        }
                        else -> {viewModel.onEvent(event)}
                    }
                })
            }

            composable(route = Routes.CREATE_GROUP_DISH){
                val viewModel = hiltViewModel<AndroidCreateGroupDishViewModel>()
                val state by viewModel.state.collectAsState()

                CreateDishScreen(state = state, onEvent = { event ->
                    when(event){
                        is CreateDishEvent.CreateDish -> {
                            viewModel.onEvent(
                                CreateDishEvent.CreateDish{
                                    println("MainActivity CreateGroupDish: onCreated")
                                    navController.navigate(Routes.GROUP_ARGS.format(state.groupId)) {
                                        popUpTo(Routes.GROUP_ARGS.format(state.groupId)) {
                                            inclusive = true
                                        }
                                    }
                                })
                        }
                        is CreateDishEvent.BackButtonPressed -> {
                            navController.popBackStack()
                        }
                        else -> viewModel.onEvent(event)
                    }
                })
            }

            composable(route = Routes.JOIN_GROUP){
                val viewModel = hiltViewModel<AndroidJoinGroupViewModel>()
                val state by viewModel.state.collectAsState()

                JoinGroupScreen(state = state, onEvent = { event ->
                    when(event){
                        JoinGroupEvent.BackButtonPressed -> {
                            if(state.isJoined){
                                navDrawerViewModel.onEvent(NavigationDrawerEvent.OnGroupAdded)
                                state.group?.let { navController.navigate(Routes.GROUP_ARGS.format(it.id)) }
                            }
                            else navController.popBackStack()
                        }
                        JoinGroupEvent.NavigateToJoinedGroup -> {
                            navDrawerViewModel.onEvent(NavigationDrawerEvent.OnGroupAdded)
                            state.group?.let { navController.navigate(Routes.GROUP_ARGS.format(it.id)) }
                        }
                        else -> {viewModel.onEvent(event)}
                    }
                })
            }

            composable(route = Routes.GROUP){
                /*val navDrawerViewModel = hiltViewModel<AndroidNavigationDrawerViewModel>()
                val navDrawerState by navDrawerViewModel.state.collectAsState()*/

                val viewModel = hiltViewModel<AndroidGroupDishListViewModel>()
                val state by viewModel.state.collectAsState()

                GroupDishListScreen(
                    state = state,
                    onEvent = {  event ->
                        when(event) {
                            is DishListEvent.SelectDish -> {
                                navController.navigate(Routes.GROUP_DISH_DETAILS_ARGS.format(event.dishId))
                            }
                            DishListEvent.CreateDish -> {
                                navController.navigate(Routes.CREATE_GROUP_DISH_ARGS.format(state.groupId))
                            }
                            else -> {viewModel.onEvent(event)}
                        } },
                    navDrawerState = navDrawerState,
                    navDrawerOnEvent = { event ->
                        when(event){
                            NavigationDrawerEvent.CreateGroup -> {
                                navController.navigate(Routes.CREATE_GROUP)
                            }
                            NavigationDrawerEvent.JoinGroup -> {
                                navController.navigate(Routes.JOIN_GROUP)
                            }
                            is NavigationDrawerEvent.NavigateToGroup -> {
                                navDrawerViewModel.onEvent(event)
                                navController.navigate(Routes.GROUP_ARGS.format(event.id)){
                                    popUpTo(navController.graph.id){
                                        inclusive = true
                                    }
                                }
                            }
                            NavigationDrawerEvent.NavigateToSettings -> TODO()
                            NavigationDrawerEvent.NavigateToSolo -> {
                                navDrawerViewModel.onEvent(event)
                                navController.navigate(Routes.DISH_LIST){
                                    popUpTo(navController.graph.id){
                                        inclusive = true
                                    }
                                }
                            }
                            else -> { navDrawerViewModel.onEvent(event)}
                        }
                    },
                    groupOnEvent = { event ->
                        when(event){
                            is GroupDishListEvent.DisbandGroup -> {
                                viewModel.onEvent(
                                    GroupDishListEvent.DisbandGroup {
                                        navDrawerViewModel.onEvent(NavigationDrawerEvent.OnGroupAdded)
                                        navController.navigate(Routes.DISH_LIST)
                                    }
                                )
                            }
                            is GroupDishListEvent.LeaveGroup -> {
                                viewModel.onEvent(
                                    GroupDishListEvent.LeaveGroup {
                                        navDrawerViewModel.onEvent(NavigationDrawerEvent.OnGroupAdded)
                                        navController.navigate(Routes.DISH_LIST)
                                    }
                                )
                            }
                            else -> {viewModel.onEvent(event)}
                        }
                    }
                )
            }

            composable(route = Routes.CREATE_GROUP){
                val viewModel = hiltViewModel<AndroidCreateGroupViewModel>()
                val state by viewModel.state.collectAsState()

                CreateGroupScreen(state = state, onEvent = { event ->
                    when(event){
                        CreateGroupEvent.BackButtonPressed -> {
                            if(state.isCreated){
                                navDrawerViewModel.onEvent(NavigationDrawerEvent.OnGroupAdded)
                                navController.navigate(Routes.GROUP_ARGS.format(state.groupId))
                            }
                            else navController.popBackStack()
                        }
                        CreateGroupEvent.NavigateToCreatedGroup -> {
                            navDrawerViewModel.onEvent(NavigationDrawerEvent.OnGroupAdded)
                            navController.navigate(Routes.GROUP_ARGS.format(state.groupId))
                        }
                        else -> {viewModel.onEvent(event)}
                    }
                })
            }

            composable(route = Routes.LOGIN) {
                val viewModel = hiltViewModel<AndroidLoginViewModel>()
                val state by viewModel.state.collectAsState()

                LoginScreen(state = state, onEvent = { event ->
                    when(event){
                        is LoginEvent.GoogleLogin -> {
                            viewModel.onEvent(
                                LoginEvent.GoogleLogin(
                                    idToken = event.idToken,
                                    onSuccess = {
                                        navController.navigate(Routes.DISH_LIST){
                                            popUpTo(Routes.DISH_LIST){
                                                inclusive = true
                                            }
                                        }
                                        navDrawerViewModel.onEvent(NavigationDrawerEvent.OnGroupAdded)
                                    }
                                )
                            )
                        }
                        is LoginEvent.SaveProfile -> {
                            viewModel.onEvent(
                                LoginEvent.SaveProfile(
                                    onSuccess = {
                                        navController.navigate(Routes.DISH_LIST){
                                            popUpTo(Routes.DISH_LIST){
                                                inclusive = true
                                            }
                                        }
                                        navDrawerViewModel.onEvent(NavigationDrawerEvent.OnGroupAdded)
                                    }
                                )
                            )
                        }
                        else -> {viewModel.onEvent(event)}
                    }
                })
            }

            composable(route = Routes.CREATE_DISH) {
                val viewModel = hiltViewModel<AndroidCreateDishViewModel>()
                val state by viewModel.state.collectAsState()

                CreateDishScreen(state = state, onEvent = { event ->
                    when(event){
                        is CreateDishEvent.CreateDish -> {
                            viewModel.onEvent(
                                CreateDishEvent.CreateDish{
                                navController.navigate(Routes.DISH_LIST) {
                                    popUpTo(Routes.DISH_LIST) {
                                        inclusive = true
                                    }
                                }
                            })
                        }
                        is CreateDishEvent.BackButtonPressed -> {
                            navController.popBackStack()
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
                }, navDrawerState = navDrawerState, navDrawerOnEvent = { event ->
                    when(event){
                        NavigationDrawerEvent.CreateGroup -> {
                            if(navDrawerState.isUserLoggedIn) navController.navigate(Routes.CREATE_GROUP)
                            else navController.navigate(Routes.LOGIN)
                        }
                        NavigationDrawerEvent.JoinGroup -> {
                            if(navDrawerState.isUserLoggedIn) navController.navigate(Routes.JOIN_GROUP)
                            else navController.navigate(Routes.LOGIN)
                        }
                        is NavigationDrawerEvent.NavigateToGroup -> {
                            navDrawerViewModel.onEvent(event)
                            navController.navigate(Routes.GROUP_ARGS.format(event.id)) {
                                popUpTo(navController.graph.id){
                                    inclusive = true
                                }
                            }
                        }
                        NavigationDrawerEvent.NavigateToSettings -> TODO()
                        NavigationDrawerEvent.NavigateToSolo -> {}
                        else -> { navDrawerViewModel.onEvent(event)}
                    }
                }
                )
            }

            composable(Routes.DISH_DETAILS){
                val viewModel = hiltViewModel<AndroidDishDetailViewModel>()
                val state by viewModel.state.collectAsState()

                DishDetailScreen(state = state, onEvent = {event ->
                    when(event){
                        is DishDetailEvent.BackButtonPressed -> {
                            navController.popBackStack()
                        }
                        is DishDetailEvent.EditButtonPressed -> {
                            navController.navigate(Routes.DISH_EDIT_ARGS.format(event.dishId))
                        }
                        else -> { viewModel.onEvent(event) }
                    }

                })
            }

            composable(Routes.DISH_EDIT) { backStackEntry ->
                val viewModel = hiltViewModel<AndroidDishEditViewModel>()
                val state by viewModel.state.collectAsState()
                val dishId = backStackEntry.arguments?.getString("dishId")?.toLong() ?: -1

                EditDishScreen(state = state, onEvent = { event ->
                    when(event){
                        is DishEditEvent.BackButtonPressed -> {
                            navController.popBackStack()
                        }
                        is DishEditEvent.EditDish -> {
                            viewModel.onEvent(
                                DishEditEvent.EditDish {
                                    navController.navigate(Routes.DISH_DETAILS_ARGS.format(dishId)) {
                                        popUpTo(Routes.DISH_LIST)
                                    }
                                }
                            )
                        }
                        is DishEditEvent.DeleteDish -> {
                            viewModel.onEvent(
                                DishEditEvent.DeleteDish {
                                    navController.navigate(Routes.DISH_LIST){
                                        popUpTo(Routes.DISH_LIST) {
                                            inclusive = true
                                        }
                                    }
                                }
                            )
                        }
                        else -> { viewModel.onEvent(event)}
                    }
                })
            }
        }
    }

}



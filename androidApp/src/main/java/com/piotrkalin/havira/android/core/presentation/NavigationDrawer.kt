package com.piotrkalin.havira.android.core.presentation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.piotrkalin.havira.core.presentation.NavigationDrawerEvent
import com.piotrkalin.havira.core.presentation.NavigationDrawerState
import com.piotrkalin.havira.android.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawer(
    state: NavigationDrawerState,
    onEvent: (NavigationDrawerEvent) -> Unit,
    content: @Composable () -> Unit
) {
    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed,
        confirmStateChange = {
            println("NavigationDrawer: confirmStateChange with value ${it}")
            if(it == DrawerValue.Open) onEvent(NavigationDrawerEvent.OpenDrawer)
            else onEvent(NavigationDrawerEvent.CloseDrawer)
            true
        }
    )
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = state.isDrawerOpen){
        if (state.isDrawerOpen) drawerState.open()
        else drawerState.close()
    }

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(12.dp))
                NavigationDrawerItem(
                    icon = { Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = stringResource(id = R.string.navigate_to_my_meals_button_description)
                    ) },
                    label = { Text(text = stringResource(id = R.string.navigate_to_my_meals_button_label)) },
                    selected = state.isSoloSelected,
                    onClick = {
                        onEvent(NavigationDrawerEvent.CloseDrawer)
                        onEvent(NavigationDrawerEvent.NavigateToSolo)
                              },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                for (group in state.groups){
                    println("NavigationDrawer: groupId - ${group.id}; selectedGroup - ${state.selectedGroup};")
                    NavigationDrawerItem(
                        icon = { Icon(imageVector = Icons.Filled.People, contentDescription = "") },
                        label = { Text(text = group.name) },
                        selected = state.selectedGroup == group.id,
                        onClick = {
                                onEvent(NavigationDrawerEvent.CloseDrawer)
                                onEvent(NavigationDrawerEvent.NavigateToGroup(group.id))
                             },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
                Divider(modifier = Modifier.padding(vertical = 4.dp))
                NavigationDrawerItem(
                    icon = { Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(id = R.string.create_group_button_description)
                    ) },
                    label = { Text(text = stringResource(id = R.string.create_group_button_label)) },
                    selected = false,
                    onClick = {
                            onEvent(NavigationDrawerEvent.CloseDrawer)
                            onEvent(NavigationDrawerEvent.CreateGroup)
                         },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                NavigationDrawerItem(
                    icon = { Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(id = R.string.join_group_button_description)
                    ) },
                    label = { Text(text = stringResource(id = R.string.join_group_button_label)) },
                    selected = false,
                    onClick = {
                        onEvent(NavigationDrawerEvent.CloseDrawer)
                        onEvent(NavigationDrawerEvent.JoinGroup)
                     },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                Divider(modifier = Modifier.padding(vertical = 4.dp))
                NavigationDrawerItem(
                    icon = { Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = stringResource(id = R.string.settings_button_description)
                    ) },
                    label = { Text(text = stringResource(id = R.string.settings_button_label)) },
                    selected = false,
                    onClick = {
                        onEvent(NavigationDrawerEvent.CloseDrawer)
                        onEvent(NavigationDrawerEvent.NavigateToSettings)
                     },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        },
        content = content,
        drawerState = drawerState
    )
}
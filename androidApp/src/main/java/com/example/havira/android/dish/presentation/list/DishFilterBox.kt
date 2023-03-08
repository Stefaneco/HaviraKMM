package com.example.havira.android.dish.presentation.list

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.North
import androidx.compose.material.icons.filled.South
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.havira.android.R
import com.example.havira.core.domain.util.SortDirection
import com.example.havira.core.domain.util.SortType
import com.example.havira.dish.presentation.list.DishListEvent
import com.example.havira.dish.presentation.list.DishListState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DishFilterBox(
    state: DishListState,
    onEvent: (DishListEvent) -> Unit,
    /*containerColor: Color = TopAppBarSmallTokens.ContainerColor.toColor(),
    scrolledContainerColor: Color = MaterialTheme.colorScheme.applyTonalElevation(
        backgroundColor = containerColor,
        elevation = TopAppBarSmallTokens.OnScrollContainerElevation
    )*/
    containerColor: Color = MaterialTheme.colorScheme.surface,
    scrolledContainerColor: Color = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
    scrollBehavior: TopAppBarScrollBehavior?
) {
    val colorTransitionFraction = scrollBehavior?.state?.overlappedFraction ?: 0f
    val fraction = if (colorTransitionFraction > 0.01f) 1f else 0f
    val appBarContainerColor by animateColorAsState(
        targetValue = containerColor(fraction, containerColor, scrolledContainerColor),
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = appBarContainerColor)
            .padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Column() {
            FilterChip(
                selected = true,
                onClick = { onEvent(DishListEvent.OpenSortDropdown) },
                label = { when(state.selectedSort){
                    SortType.RATING -> Text(text = stringResource(id = R.string.rating))
                    SortType.LAST_MADE -> Text(text = stringResource(id = R.string.last_made_date))
                    SortType.CREATED -> Text(text = stringResource(id = R.string.created_date))
                    SortType.NOF_RATINGS -> Text(text = stringResource(id = R.string.nof_ratings))
                    SortType.TITLE -> Text(text = stringResource(id = R.string.title))
                }},
                leadingIcon = {
                    Icon(imageVector = Icons.Filled.KeyboardArrowDown, contentDescription = "")
                }
            )
            DropdownMenu(
                expanded = state.isSortingDropdownDisplayed,
                onDismissRequest = { onEvent(DishListEvent.DismissSortDropdown) }) {
                DropdownMenuItem(
                    text = { Text(text = stringResource(id = R.string.rating)) },
                    onClick = { onEvent(DishListEvent.SelectSortType(SortType.RATING)) }
                )
                DropdownMenuItem(
                    text = { Text(text = stringResource(id = R.string.last_made_date)) },
                    onClick = { onEvent(DishListEvent.SelectSortType(SortType.LAST_MADE)) }
                )
                DropdownMenuItem(
                    text = { Text(text = stringResource(id = R.string.created_date)) },
                    onClick = { onEvent(DishListEvent.SelectSortType(SortType.CREATED)) }
                )
                DropdownMenuItem(
                    text = { Text(text = stringResource(id = R.string.nof_ratings)) },
                    onClick = { onEvent(DishListEvent.SelectSortType(SortType.NOF_RATINGS)) }
                )
                DropdownMenuItem(
                    text = { Text(text = stringResource(id = R.string.title)) },
                    onClick = { onEvent(DishListEvent.SelectSortType(SortType.TITLE)) }
                )
            }
        }
        Spacer(modifier = Modifier.padding(horizontal = 4.dp))

        FilterChip(
            selected = state.selectedSortDirection == SortDirection.DESC,
            onClick = { onEvent(DishListEvent.SelectSortDirection(SortDirection.DESC)) },
            label = { Text(text = "ASC")},
            leadingIcon = { Icon(imageVector = Icons.Filled.North, contentDescription = "")}
        )
        Spacer(modifier = Modifier.padding(horizontal = 4.dp))

        FilterChip(
            selected = state.selectedSortDirection == SortDirection.ASC,
            onClick = { onEvent(DishListEvent.SelectSortDirection(SortDirection.ASC)) },
            label = { Text(text = "DESC")},
            leadingIcon = { Icon(imageVector = Icons.Filled.South, contentDescription = "")}
        )
    }
}

@Composable
fun containerColor(colorTransitionFraction: Float, containerColor: Color, scrolledContainerColor: Color): Color {
    return lerp(
        containerColor,
        scrolledContainerColor,
        FastOutLinearInEasing.transform(colorTransitionFraction)
    )
}
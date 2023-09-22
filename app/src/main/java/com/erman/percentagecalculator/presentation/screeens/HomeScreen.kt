package com.erman.percentagecalculator.presentation.screeens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.erman.percentagecalculator.R
import com.erman.percentagecalculator.model.MenuItem
import com.erman.percentagecalculator.presentation.AppBar
import com.erman.percentagecalculator.presentation.navigation.ScreenRoute
import com.erman.percentagecalculator.presentation.theme.Shapes
import com.erman.percentagecalculator.presentation.theme.Typography

@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(topBar = {
        AppBar(stringResource(id = R.string.app_name))
    }) {
        MenuItemList(modifier = Modifier.padding(it), onMenuItemClick = { menuItem ->
            when (menuItem.route) {
                ScreenRoute.FIND_PERCENTAGE -> navController.navigate(ScreenRoute.FIND_PERCENTAGE.name)
                else -> Log.e("Clicked", menuItem.contentDescription ?: "-")
            }
        })
    }
}

@Composable
fun MenuBody(
    items: List<MenuItem>,
    modifier: Modifier = Modifier,
    onItemClick: (MenuItem) -> Unit
) {
    LazyColumn(modifier) {
        items(items) { item ->
            Column(modifier = Modifier.padding(4.dp)) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onItemClick(item)
                        },
                    shape = Shapes.medium,
                    elevation = 2.dp
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Row(modifier = Modifier.padding(vertical = 8.dp)) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = item.iconResId),
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = item.title,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(top = 4.dp)
                            )
                        }
                        Text(text = item.description, style = Typography.body2)
                        Spacer(modifier = Modifier.padding(bottom = 8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun MenuItemList(
    modifier: Modifier = Modifier,
    onMenuItemClick: (MenuItem) -> Unit
) {
    val items = listOf(
        MenuItem(
            route = ScreenRoute.FIND_PERCENTAGE,
            title = stringResource(id = R.string.find_percentage),
            description = stringResource(id = R.string.find_percentage_description),
            iconResId = R.drawable.ic_percent
        ),
        MenuItem(
            route = ScreenRoute.PERCENTAGE_OF_VALUE,
            title = stringResource(id = R.string.find_percentage_of_value),
            description = stringResource(id = R.string.find_percentage_of_value_description),
            iconResId = R.drawable.ic_pie
        ),
        MenuItem(
            route = ScreenRoute.INCREASE_PERCENTAGE,
            title = stringResource(id = R.string.increase_value_by_percentage),
            description = stringResource(id = R.string.increase_value_by_percentage_description),
            iconResId = R.drawable.ic_increase
        ),
        MenuItem(
            route = ScreenRoute.DECREASE_PERCENTAGE,
            title = stringResource(id = R.string.decrease_value_by_percentage),
            description = stringResource(id = R.string.decrease_value_by_percentage_description),
            iconResId = R.drawable.ic_decrease
        ),
        MenuItem(
            route = ScreenRoute.PERCENTAGE_CHANGE,
            title = stringResource(id = R.string.find_percentage_change),
            description = stringResource(id = R.string.find_percentage_change_description),
            iconResId = R.drawable.ic_change
        ),
        MenuItem(
            route = ScreenRoute.MARGIN,
            title = stringResource(id = R.string.margin),
            description = stringResource(id = R.string.margin_description),
            iconResId = R.drawable.ic_margin
        ),
        MenuItem(
            route = ScreenRoute.FRACTION_TO_PERCENTAGE,
            title = stringResource(id = R.string.fraction_to_percentage),
            description = stringResource(id = R.string.fraction_to_percentage_description),
            iconResId = R.drawable.ic_fraction
        ),
        MenuItem(
            route = ScreenRoute.TIP_SPLIT,
            title = stringResource(id = R.string.tip_split),
            description = stringResource(id = R.string.tip_split_description),
            iconResId = R.drawable.ic_tip
        )
    )

    MenuBody(items = items, modifier = modifier, onItemClick = {
        onMenuItemClick(it)
    })
}

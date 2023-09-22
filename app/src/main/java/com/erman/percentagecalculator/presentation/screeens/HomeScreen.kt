package com.erman.percentagecalculator.presentation.screeens

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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.erman.percentagecalculator.BuildConfig
import com.erman.percentagecalculator.R
import com.erman.percentagecalculator.SEPARATOR
import com.erman.percentagecalculator.model.MenuItem
import com.erman.percentagecalculator.presentation.AppBar
import com.erman.percentagecalculator.presentation.navigation.NavigationDestination
import com.erman.percentagecalculator.presentation.navigation.Operation
import com.erman.percentagecalculator.presentation.theme.Shapes
import com.erman.percentagecalculator.presentation.theme.Typography
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(topBar = {
        AppBar(navController, stringResource(id = R.string.app_name))
    }) {
        MenuItemList(modifier = Modifier.padding(it), onMenuItemClick = { menuItem ->
            if (menuItem is MenuItem.Advertisement) {
                return@MenuItemList
            }

            when ((menuItem as MenuItem.OperationMenuItem).operation) {
                Operation.FIND_PERCENTAGE, Operation.PERCENTAGE_OF_VALUE, Operation.INCREASE_PERCENTAGE,
                Operation.DECREASE_PERCENTAGE, Operation.PERCENTAGE_CHANGE, Operation.FRACTION_TO_PERCENTAGE -> {
                    navController.navigate(
                        NavigationDestination.CalculationScreen.route + SEPARATOR + menuItem.operation
                    )
                }
            }
        })
    }
}

@Composable
private fun MenuItemList(
    modifier: Modifier = Modifier,
    onMenuItemClick: (MenuItem) -> Unit
) {
    val items = mutableListOf<MenuItem>(
        MenuItem.OperationMenuItem(
            operation = Operation.FIND_PERCENTAGE,
            title = stringResource(id = R.string.calculate_percentage),
            description = stringResource(id = R.string.find_percentage_description),
            iconResId = R.drawable.ic_percent
        ),
        MenuItem.OperationMenuItem(
            operation = Operation.PERCENTAGE_OF_VALUE,
            title = stringResource(id = R.string.calculate_percentage_of_value),
            description = stringResource(id = R.string.find_percentage_of_value_description),
            iconResId = R.drawable.ic_pie
        ),
        MenuItem.OperationMenuItem(
            operation = Operation.INCREASE_PERCENTAGE,
            title = stringResource(id = R.string.increase_value_by_percentage),
            description = stringResource(id = R.string.increase_value_by_percentage_description),
            iconResId = R.drawable.ic_increase
        ),
        MenuItem.OperationMenuItem(
            operation = Operation.DECREASE_PERCENTAGE,
            title = stringResource(id = R.string.decrease_value_by_percentage),
            description = stringResource(id = R.string.decrease_value_by_percentage_description),
            iconResId = R.drawable.ic_decrease
        ),
        MenuItem.OperationMenuItem(
            operation = Operation.PERCENTAGE_CHANGE,
            title = stringResource(id = R.string.calculate_percentage_change),
            description = stringResource(id = R.string.find_percentage_change_description),
            iconResId = R.drawable.ic_change
        ),
        MenuItem.OperationMenuItem(
            operation = Operation.FRACTION_TO_PERCENTAGE,
            title = stringResource(id = R.string.fraction_to_percentage),
            description = stringResource(id = R.string.fraction_to_percentage_description),
            iconResId = R.drawable.ic_fraction
        )
    )
    items.add((items.indices).random(), MenuItem.Advertisement)

    MenuBody(items = items, modifier = modifier, onItemClick = {
        onMenuItemClick(it)
    })
}

@Composable
private fun MenuBody(
    items: List<MenuItem>,
    modifier: Modifier = Modifier,
    onItemClick: (MenuItem) -> Unit
) {
    Column(modifier = modifier) {
        LazyColumn(
            Modifier
                .weight(1F)
                .padding(top = 4.dp)
        ) {
            items(items) { item ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .clickable {
                            onItemClick(item)
                        },
                    shape = Shapes.medium, elevation = 2.dp
                ) {
                    MenuContent(modifier = Modifier.padding(8.dp), item = item)
                }
            }
        }
    }
}

@Composable
private fun MenuContent(modifier: Modifier = Modifier, item: MenuItem) {
    Column {
        when (item) {
            is MenuItem.OperationMenuItem -> {
                Column(modifier = modifier) {
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
            else -> {
                AdSection(Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
private fun AdSection(modifier: Modifier = Modifier) {
    AndroidView(modifier = modifier, factory = { context ->
        AdView(context).apply {
            setAdSize(AdSize.LARGE_BANNER)
            adUnitId = if (BuildConfig.DEBUG) {
                context.getString(R.string.test_ad_unit_id)
            } else {
                context.getString(R.string.home_ad_unit_id)
            }
            loadAd(AdRequest.Builder().build())
        }
    })
}

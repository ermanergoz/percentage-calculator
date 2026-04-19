package com.erman.percentagecalculator.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.viewinterop.AndroidView
import com.erman.percentagecalculator.BuildConfig
import com.erman.percentagecalculator.R
import com.erman.percentagecalculator.domain.model.Operation
import com.erman.percentagecalculator.presentation.AppBar
import com.erman.percentagecalculator.presentation.Dimens
import com.erman.percentagecalculator.presentation.MenuItem
import com.erman.percentagecalculator.presentation.descriptionResId
import com.erman.percentagecalculator.presentation.iconResId
import com.erman.percentagecalculator.presentation.theme.Shapes
import com.erman.percentagecalculator.presentation.titleResId
import com.erman.percentagecalculator.presentation.viewModel.HomeViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import org.koin.androidx.compose.koinViewModel

private const val FIRST_AD_INDEX: Int = 3
private const val SECOND_AD_INDEX: Int = 10

@Composable
fun HomeScreen(
    onOperationClick: (Operation) -> Unit,
    onHistoryClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onAboutClick: () -> Unit,
) {
    val homeViewModel: HomeViewModel = koinViewModel()
    val homeState by homeViewModel.state.collectAsState()
    val operations = homeState.operations.map { MenuItem.OperationItem(it) }
    val items = remember(operations) { buildItemsWithAds(operations) }
    var showMenu by remember { mutableStateOf(false) }

    Scaffold(topBar = {
        AppBar(
            titleText = stringResource(id = R.string.app_name),
            actions = {
                IconButton(onClick = onHistoryClick) {
                    Icon(
                        painter = painterResource(R.drawable.ic_history),
                        contentDescription = stringResource(R.string.history),
                        tint = MaterialTheme.colors.onPrimary,
                    )
                }
                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = null,
                            tint = MaterialTheme.colors.onPrimary,
                        )
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                    ) {
                        DropdownMenuItem(onClick = {
                            showMenu = false
                            onSettingsClick()
                        }) {
                            Text(text = stringResource(R.string.settings))
                        }
                        DropdownMenuItem(onClick = {
                            showMenu = false
                            onAboutClick()
                        }) {
                            Text(text = stringResource(R.string.about))
                        }
                    }
                }
            },
        )
    }) { padding ->
        OperationList(
            items = items,
            modifier =
                Modifier
                    .padding(padding)
                    .navigationBarsPadding(),
            onOperationClick = onOperationClick,
        )
    }
}

@Composable
private fun OperationList(
    items: List<MenuItem>,
    modifier: Modifier = Modifier,
    onOperationClick: (Operation) -> Unit,
) {
    val context = LocalContext.current
    val adUnitStrings =
        items.filterIsInstance<MenuItem.Advertisement>().associate { ad ->
            ad.adUnitIdRes to stringResource(ad.adUnitIdRes)
        }
    val testAdUnitId = stringResource(R.string.test_ad_unit_id)
    val adViews =
        remember {
            items.filterIsInstance<MenuItem.Advertisement>().associate { ad ->
                ad.adUnitIdRes to
                    AdView(context).apply {
                        setAdSize(AdSize.LARGE_BANNER)
                        adUnitId =
                            if (BuildConfig.DEBUG) {
                                testAdUnitId
                            } else {
                                adUnitStrings[ad.adUnitIdRes] ?: ""
                            }
                        loadAd(AdRequest.Builder().build())
                    }
            }
        }

    Column(modifier = modifier) {
        LazyColumn(
            Modifier
                .weight(1F)
                .padding(top = Dimens.spacingSm),
        ) {
            items(
                count = items.size,
                key = { index ->
                    when (val item = items[index]) {
                        is MenuItem.OperationItem -> item.operation.name
                        is MenuItem.Advertisement -> "ad_${item.adUnitIdRes}"
                    }
                },
            ) { index ->
                when (val item = items[index]) {
                    is MenuItem.OperationItem -> {
                        OperationCard(
                            operation = item.operation,
                            onClick = { onOperationClick(item.operation) },
                        )
                    }

                    is MenuItem.Advertisement -> {
                        Surface(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .height(Dimens.operationCardHeight)
                                    .padding(
                                        horizontal = Dimens.spacingSm,
                                        vertical = Dimens.spacingXs,
                                    ),
                            shape = Shapes.medium,
                            elevation = Dimens.elevationSm,
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center,
                            ) {
                                val adView = adViews[item.adUnitIdRes]
                                if (adView != null) {
                                    AndroidView(
                                        modifier = Modifier.fillMaxWidth(),
                                        factory = { adView },
                                    )
                                }
                            }
                        }
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(Dimens.spacingXl)) }
        }
    }
}

@Composable
private fun OperationCard(
    operation: Operation,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier =
            modifier
                .fillMaxWidth()
                .height(Dimens.operationCardHeight)
                .padding(horizontal = Dimens.spacingSm, vertical = Dimens.spacingXs)
                .clickable { onClick() }
                .semantics { role = Role.Button },
        shape = Shapes.medium,
        elevation = Dimens.elevationSm,
    ) {
        Column(
            modifier = Modifier.padding(Dimens.spacingMd),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = operation.iconResId),
                    contentDescription = null,
                    tint = MaterialTheme.colors.primary,
                )
                Spacer(modifier = Modifier.width(Dimens.spacingMd))
                Text(
                    text = stringResource(operation.titleResId),
                    style = MaterialTheme.typography.body1,
                )
            }
            Spacer(modifier = Modifier.height(Dimens.spacingSm))
            Text(
                text = stringResource(operation.descriptionResId),
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
            )
        }
    }
}

private fun buildItemsWithAds(operations: List<MenuItem.OperationItem>): List<MenuItem> {
    return operations.toMutableList<MenuItem>().apply {
        add(FIRST_AD_INDEX, MenuItem.Advertisement(R.string.home_ad_unit_id))
        add(SECOND_AD_INDEX, MenuItem.Advertisement(R.string.home_ad_unit_id_2))
    }
}

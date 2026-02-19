package com.erman.percentagecalculator.presentation.screens

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.core.net.toUri
import com.erman.percentagecalculator.BuildConfig
import com.erman.percentagecalculator.R
import com.erman.percentagecalculator.presentation.AppBar
import com.erman.percentagecalculator.presentation.Dimens

private const val PLAY_STORE_URL_PREFIX = "https://play.google.com/store/apps/details?id="
private const val PLAY_STORE_MARKET_PREFIX = "market://details?id="

@Composable
fun AboutScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    val githubUrl = stringResource(id = R.string.github_repo_url)
    val privacyUrl = stringResource(id = R.string.privacy_policy_url)
    val packageName = context.packageName

    Scaffold(topBar = {
        AppBar(
            titleText = stringResource(id = R.string.about),
            onBackClick = onBackClick,
        )
    }) { padding ->
        Column(
            modifier =
                Modifier
                    .padding(padding)
                    .navigationBarsPadding()
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(Dimens.spacingMd),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(Dimens.spacingXl))
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_percent),
                contentDescription = null,
                modifier = Modifier.size(Dimens.iconLg),
                tint = MaterialTheme.colors.primary,
            )
            Spacer(modifier = Modifier.height(Dimens.spacingMd))
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.h5,
            )
            Spacer(modifier = Modifier.height(Dimens.spacingSm))
            Text(
                text = "${stringResource(id = R.string.version)} ${BuildConfig.VERSION_NAME}",
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
            )
            Spacer(modifier = Modifier.height(Dimens.spacingXs))
            Text(
                text = "${stringResource(id = R.string.developer)}: ${stringResource(id = R.string.developer_name)}",
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
            )
            Spacer(modifier = Modifier.height(Dimens.spacingXl))
            Divider()
            Spacer(modifier = Modifier.height(Dimens.spacingMd))
            OutlinedButton(
                onClick = {
                    val uri = "$PLAY_STORE_MARKET_PREFIX$packageName".toUri()
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    try {
                        context.startActivity(intent)
                    } catch (_: android.content.ActivityNotFoundException) {
                        context.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                "$PLAY_STORE_URL_PREFIX$packageName".toUri(),
                            ),
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(id = R.string.rate_app))
            }
            Spacer(modifier = Modifier.height(Dimens.spacingSm))
            OutlinedButton(
                onClick = {
                    context.startActivity(Intent(Intent.ACTION_VIEW, githubUrl.toUri()))
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(id = R.string.source_code))
            }
            Spacer(modifier = Modifier.height(Dimens.spacingSm))
            OutlinedButton(
                onClick = {
                    context.startActivity(Intent(Intent.ACTION_VIEW, privacyUrl.toUri()))
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = stringResource(id = R.string.privacy_policy))
            }
        }
    }
}

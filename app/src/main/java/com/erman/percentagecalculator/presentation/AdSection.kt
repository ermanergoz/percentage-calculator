package com.erman.percentagecalculator.presentation

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.erman.percentagecalculator.BuildConfig
import com.erman.percentagecalculator.R
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

@Composable
fun AdSection(
    modifier: Modifier = Modifier,
    adSize: AdSize,
    @StringRes adUnitIdRes: Int,
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            AdView(context).apply {
                setAdSize(adSize)
                adUnitId =
                    if (BuildConfig.DEBUG) {
                        context.getString(R.string.test_ad_unit_id)
                    } else {
                        context.getString(adUnitIdRes)
                    }
                loadAd(AdRequest.Builder().build())
            }
        },
    )
}

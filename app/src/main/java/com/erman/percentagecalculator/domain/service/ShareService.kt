package com.erman.percentagecalculator.domain.service

import android.content.Context

interface ShareService {
    fun shareResult(
        context: Context,
        text: String,
    )
}

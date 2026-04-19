package com.erman.percentagecalculator.data

import android.content.Context
import android.content.Intent
import com.erman.percentagecalculator.domain.service.ShareService

class ShareServiceImpl : ShareService {
    override fun shareResult(
        context: Context,
        text: String,
    ) {
        context.startActivity(
            Intent.createChooser(
                Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, text)
                },
                null,
            ),
        )
    }
}

package com.maddy.jetpackbookreader.common.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PanelizationSummary(
    val containsEpubBubbles: Boolean,
    val containsImageBubbles: Boolean
) : Parcelable
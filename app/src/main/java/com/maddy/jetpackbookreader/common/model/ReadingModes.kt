package com.maddy.jetpackbookreader.common.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ReadingModes(
    val image: Boolean,
    val text: Boolean
) : Parcelable
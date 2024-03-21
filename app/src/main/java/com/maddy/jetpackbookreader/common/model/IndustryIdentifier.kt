package com.maddy.jetpackbookreader.common.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class IndustryIdentifier(
    val identifier: String,
    val type: String
) : Parcelable
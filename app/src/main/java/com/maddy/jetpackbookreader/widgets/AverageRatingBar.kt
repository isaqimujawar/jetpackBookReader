package com.maddy.jetpackbookreader.widgets

import android.view.MotionEvent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.maddy.jetpackbookreader.R

@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun AverageRatingBar(
    modifier: Modifier = Modifier,
    rating: Int = 0,
    onClick: (Int) -> Unit = {}
) {
    var selectedState by rememberSaveable { mutableStateOf(false) }
    val starSize by animateDpAsState(
        targetValue = if (selectedState) 42.dp else 34.dp,
        animationSpec = spring(Spring.DampingRatioMediumBouncy), label = "spring_bouncy"
    )

    Row(
        modifier = Modifier
            .padding(horizontal = 1.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..5) {
            Icon(
                painter = painterResource(id = R.drawable.star_icon),
                contentDescription = stringResource(R.string.star_icon),
                tint = if (rating >= i) Color(0xFFFFD700) else Color(0xFFA2ADB1),
                modifier = modifier
                    .size(size = starSize)
                    .pointerInteropFilter {
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> { selectedState = true }

                            MotionEvent.ACTION_UP -> { selectedState = false }
                        }
                        true
                    },
            )
        }
    }
}
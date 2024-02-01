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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import com.maddy.jetpackbookreader.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BookRatingBar(
    modifier: Modifier = Modifier,
    text: String = "Rating",
    rating: Int,
    onClick: (Int) -> Unit = {}
) {
    var ratingState by rememberSaveable { mutableStateOf(rating) }
    var selectedState by rememberSaveable { mutableStateOf(false) }
    val starSize by animateDpAsState(
        targetValue = if (selectedState) 42.dp else 34.dp,
        animationSpec = spring(Spring.DampingRatioMediumBouncy), label = "spring_bouncy"
    )

    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            modifier = Modifier.width(150.dp),
            color = MaterialTheme.colorScheme.primary
        )
        for (i in 1..5) {
            Icon(
                painter = painterResource(id = R.drawable.star_icon),
                contentDescription = stringResource(R.string.star_icon),
                tint = if (ratingState >= i) Color(0xFFFFD700) else Color(0xFFA2ADB1),
                modifier = modifier
                    .size(size = starSize)
                    .pointerInteropFilter {
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> {
                                selectedState = true
                                ratingState = i
                                onClick(ratingState)
                            }

                            MotionEvent.ACTION_UP -> {
                                selectedState = false
                            }
                        }
                        true
                    },
            )
        }
    }
}
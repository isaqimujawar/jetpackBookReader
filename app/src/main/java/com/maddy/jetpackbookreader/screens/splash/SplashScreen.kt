package com.maddy.jetpackbookreader.screens.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.maddy.jetpackbookreader.R
import com.maddy.jetpackbookreader.navigation.ReaderScreens
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val alphaAnimation = remember { Animatable(initialValue = 0f) }
    LaunchedEffectSplashScreen(alphaAnimation, navController)
    Surface(
        modifier = Modifier
            .padding(15.dp)
            .size(330.dp)
            .scale(alphaAnimation.value),
        shape = CircleShape,
        color = Color.White,
        border = BorderStroke(width = 2.dp, color = Color.LightGray)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.logo),
                style = MaterialTheme.typography.titleLarge,
                color = Color.Red.copy(alpha = 0.4f)
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = stringResource(R.string.motto),
                style = MaterialTheme.typography.headlineMedium,
                color = Color.LightGray
            )
        }
    }
}

/**
 * Starts an animation on launch of a composable
 * LaunchedEffect runs when a composable enters the composition.
 * You can use this to drive the animation state change.
 * Using Animatable with the animateTo method to start the animation on launch.
 */
@Composable
private fun LaunchedEffectSplashScreen(
    scale: Animatable<Float, AnimationVector1D>,
    navController: NavController
) {
    LaunchedEffect(
        key1 = true,
        block = {
            scale.animateTo(
                targetValue = 0.9f,
                animationSpec = tween(
                    durationMillis = 500,
                    easing = { OvershootInterpolator(8f).getInterpolation(it) }
                )
            )
            delay(500L)
            navController.navigate(ReaderScreens.LoginScreen.name)
        }
    )
}
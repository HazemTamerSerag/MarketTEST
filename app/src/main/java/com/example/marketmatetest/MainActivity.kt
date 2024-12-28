package com.example.marketmatetest

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.marketmatetest.ui.theme.MarketMateTestTheme
import com.example.marketmatetest.ui.theme.White
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MarketMateTestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    SplashScreen()
                }
            }
        }
    }
}

@Composable
fun SplashScreen() {
    val context = LocalContext.current
    val alpha = remember {
        androidx.compose.animation.core.Animatable(0f)
    }
    LaunchedEffect(Unit) {
        alpha.animateTo(1f,
            animationSpec = tween(1500)
        )
        delay(2000)
        context.startActivity(Intent(context, PictureChooserActivity::class.java))
    }
    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = White
            ),
        contentAlignment = Alignment.Center
    ){
        Image(
            modifier = Modifier.alpha(alpha = alpha.value),
            painter = painterResource(id =R.drawable.logosplashmarketmate), contentDescription = stringResource(
                R.string.marketmate
            )
        )
    }
}
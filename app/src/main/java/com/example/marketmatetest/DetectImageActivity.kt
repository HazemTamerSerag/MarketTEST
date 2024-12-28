package com.example.marketmatetest

import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.example.marketmatetest.ui.theme.MarketMateTestTheme
import com.example.marketmatetest.ui.theme.White
@Suppress("DEPRECATION")
class DetectImageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MarketMateTestTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(White) // Ensures entire screen is white
                ) {
                    val effect = intent.getStringExtra("effect")
                    val photo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                        intent.getParcelableExtra("photo", Uri::class.java)!!
                    else
                        intent.getParcelableExtra("photo")!!
                    if (effect == "bw") {
                        ImageEffect(
                            photo = photo,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ImageEffect(
    photo: Uri,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(White) // Ensures the background is white
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = photo,
                imageLoader = ImageLoader(context)
            ),
            contentDescription = stringResource(R.string.photo),
            modifier = Modifier.fillMaxSize()
        )
    }
}

package com.example.marketmatetest

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.example.marketmatetest.ui.theme.MarketMateTestTheme
import com.example.marketmatetest.ui.theme.White
import java.io.File
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat


class PictureChooserActivity : ComponentActivity() {
    private val cameraPermission = Manifest.permission.CAMERA

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Permission granted, proceed with showing the PhotoPicker
                showPhotoPicker()
            } else {
                // Permission denied, show a message
                showPermissionDeniedMessage()
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        checkCameraPermission()
    }
    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, cameraPermission) == PackageManager.PERMISSION_GRANTED) {
            // Permission is already granted
            showPhotoPicker()
        } else {
            // Request camera permission
            requestPermissionLauncher.launch(cameraPermission)
        }
    }

    private fun showPhotoPicker() {
        setContent {
            MarketMateTestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PhotoPicker(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

    private fun showPermissionDeniedMessage() {
        Toast.makeText(
            this,
            "Camera permission is required to take pictures.",
            Toast.LENGTH_SHORT
        ).show()
    }
}

@Composable
fun PhotoPicker(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var picUri by remember { mutableStateOf<Uri?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    val cameraUri = remember { mutableStateOf<Uri?>(null) }
    val fileLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        picUri = uri
        if (uri != null) {
            showRandomToast(context)
        }
    }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            picUri = cameraUri.value
            showRandomToast(context)
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(White)
    ) {
        Button(onClick = { showDialog = true }) {
            Text(text = stringResource( R.string.pick_image ))
        }

        picUri?.let {
            Image(
                painter = rememberAsyncImagePainter(
                    model = picUri,
                    imageLoader = ImageLoader(context)
                ),
                contentDescription = stringResource(R.string.photo),
                modifier = Modifier
                    .width(250.dp)
                    .height(250.dp)
            )
            EffectButton(
                effect = "Detect Photo",
                picUri = picUri,
                btnText = R.string.photo
            )
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Select Image Source") },
            text = {
                Column {
                    Button(
                        onClick = {
                            val uri = context.createImageFile()
                            cameraUri.value = uri
                            cameraLauncher.launch(uri)
                            showDialog = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Camera")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            fileLauncher.launch("image/*")
                            showDialog = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Gallery")
                    }
                }
            },
            confirmButton = {},
            dismissButton = {}
        )
    }
}

fun Context.createImageFile(): Uri {
    val timeStamp = System.currentTimeMillis().toString()
    val file = File(cacheDir, "IMG_$timeStamp.jpg")
    return FileProvider.getUriForFile(
        this,
        "$packageName.provider",
        file
    )
}

fun showRandomToast(context: Context) {
    val jokes = listOf(
        context.getString(R.string.what_an_ugly_photo),
        context.getString(R.string.change_your_phone_it_s_the_worst_camera_i_ve_ever_seen),
        context.getString(R.string.good_pic_like),
        context.getString(R.string.awwwwwwwwwwww)
    )
    val randomJoke = jokes.random()
    Toast.makeText(context, randomJoke, Toast.LENGTH_SHORT).show()
}


@Composable
fun EffectButton(effect: String, picUri: Uri?, @StringRes btnText: Int) {
    val context = LocalContext.current
    Button(onClick = {
        if (picUri != null) {
            val i = Intent(context, DetectImageActivity::class.java)
            i.putExtra("effect", effect)
            i.putExtra("photo", picUri)
            context.startActivity(i)
        } else
            Toast.makeText(
                context,
                "choose an image before selecting an Detector",
                Toast.LENGTH_LONG
            ).show()

    }) {
        Text(text = stringResource(id = btnText))
    }
}


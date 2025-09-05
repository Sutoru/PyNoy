package com.example.pynoycompiler

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animate
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.DisposableEffect

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.TextUnit
import com.example.pynoycompiler.ui.theme.*
import com.example.pynoycompiler.ui.theme.PyNoySecondary
import com.example.pynoycompiler.ui.theme.PyNoyTertiary
import com.example.pynoycompiler.ui.theme.PyNoyQuarternary
import com.example.pynoycompiler.ui.theme.BackgroundWhite
import com.example.pynoycompiler.ui.theme.TextBlack
import com.example.pynoycompiler.R
import kotlinx.coroutines.delay
import androidx.compose.foundation.border
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material.icons.filled.Redo
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.asImageBitmap
import android.graphics.BitmapFactory
import android.Manifest
import android.content.pm.PackageManager
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.BufferedReader
import java.io.InputStreamReader
import android.net.Uri
import android.provider.DocumentsContract
import android.content.Context
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.platform.LocalContext
import android.content.Intent
import androidx.activity.OnBackPressedCallback
import com.example.pynoycompiler.ui.theme.PyNoyCompilerTheme
import com.example.pynoycompiler.evaluateNumericValue

data class CodeTab(
    val name: String,
    val content: String,
    val filePath: String? = null
)

@Composable
fun AnimatedIridescentBackground(isDarkMode: Boolean = true) {
    val infiniteTransition = rememberInfiniteTransition(label = "subtle")
    val animatedProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "subtle_progress"
    )

    val colors = if (isDarkMode) {
        // Dark mode colors (current theme)
        listOf(
            Color(0xFFC99CFF), // Very light violet (top)
            Color(0xFF8B5CF6), // Bright violet
            Color(0xFF7C3AED), // Darker violet
            Color(0xFF6D28D9), // Even darker violet
            Color(0xFF5B21B6), // Deep violet
            Color(0xFF4C1D95), // Very dark violet
            Color(0xFF2E1B47), // Dark violet
            Color(0xFF000000)  // Pure black (bottom)
        )
    } else {
        // Light mode colors
        listOf(
            Color(0xFFFFFFFF), // Pure white (top)
            Color(0xFFF3E8FF), // Very light violet
            Color(0xFFE9D5FF), // Light violet
            Color(0xFFDDD6FE), // Soft violet
            Color(0xFFC4B5FD), // Medium violet
            Color(0xFFA78BFA), // Bright violet
            Color(0xFF8B5CF6), // Violet
            Color(0xFF7C3AED)  // Dark violet (bottom)
        )
    }

    val gradient = Brush.linearGradient(
        colors = colors,
        start = Offset(0f, animatedProgress * 100f),
        end = Offset(0f, 2500f + animatedProgress * 100f)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
    )
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PyNoyCompilerTheme {


                PyNoyCompilerApp()
            }
        }
    }
}

fun createPyNoyFolder(context: android.content.Context) {
    try {
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val pynoyFolder = File(downloadsDir, "PyNoy")

        if (!pynoyFolder.exists()) {
            val created = pynoyFolder.mkdirs()
            if (created) {
                // Create a sample file
                val sampleFile = File(pynoyFolder, "sample.pynoy")
                FileWriter(sampleFile).use { writer ->
                    writer.write("// Sample PyNoy code\npakita()\n")
                }
            }
        }

        // Open file manager to the PyNoy folder
        val intent = android.content.Intent(android.content.Intent.ACTION_VIEW)
        intent.setDataAndType(android.net.Uri.fromFile(pynoyFolder), "*/*")
        intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)

    } catch (e: IOException) {
        e.printStackTrace()
    }
}

@Composable
fun PyNoyCompilerApp() {
    var showLoading by remember { mutableStateOf(true) }
    var currentScreen by remember { mutableStateOf("main") }
    var isDarkMode by remember { mutableStateOf(true) }
    var fontSize by remember { mutableStateOf(14.sp) }
    var fontStyle by remember { mutableStateOf("Monospace") }
    var showFileDialog by remember { mutableStateOf(false) }
    var pendingFileUri by remember { mutableStateOf<Uri?>(null) }

    // Handle back button press
    val context = LocalContext.current
    val activity = context as? ComponentActivity

    // File picker launcher for main screen
    val mainFilePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedUri ->
            try {
                val inputStream = context.contentResolver.openInputStream(selectedUri)
                val reader = BufferedReader(InputStreamReader(inputStream))
                val content = reader.readText()
                reader.close()
                inputStream?.close()

                // Check if it's a Python file
                val fileName = selectedUri.lastPathSegment ?: ""
                if (!fileName.endsWith(".py")) {
                    // Show error dialog
                    return@let
                }

                // Navigate to code editor with the file
                pendingFileUri = selectedUri
                currentScreen = "codeEditor"

            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    DisposableEffect(Unit) {
        val onBackPressedCallback = object : androidx.activity.OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when (currentScreen) {
                    "main" -> {
                        // Exit app when on main screen
                        activity?.finish()
                    }
                    "codeEditor", "settings" -> {
                        // Go back to main screen
                        currentScreen = "main"
                    }
                }
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(onBackPressedCallback)
        onDispose {
            onBackPressedCallback.remove()
        }
    }

    LaunchedEffect(Unit) {
        delay(3000) // 3 seconds loading screen
        showLoading = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
    ) {
        if (showLoading) {
            LoadingScreen(isDarkMode = isDarkMode)
        } else {
            when (currentScreen) {
                "main" -> MainScreen(
                    onCodeEditorClick = { currentScreen = "codeEditor" },
                    onSettingsClick = { currentScreen = "settings" },
                    isDarkMode = isDarkMode,
                    onFileOpen = { mainFilePickerLauncher.launch("*/*") }
                )
                "codeEditor" -> CodeEditorScreen(
                    onBackClick = { currentScreen = "main" },
                    onSaveFile = { /* TODO: Implement save file */ },
                    onLoadFile = { /* TODO: Implement load file */ },
                    onRunCode = { /* TODO: Implement run code */ },
                    isDarkMode = isDarkMode,
                    fontSize = fontSize,
                    fontStyle = fontStyle,
                    pendingFileUri = pendingFileUri,
                    onPendingFileHandled = { pendingFileUri = null }
                )
                "settings" -> SettingsScreen(
                    onBackClick = { currentScreen = "main" },
                    isDarkMode = isDarkMode,
                    onThemeChange = { isDarkMode = it },
                    fontSize = fontSize,
                    onFontSizeChange = { fontSize = it },
                    fontStyle = fontStyle,
                    onFontStyleChange = { fontStyle = it }
                )
            }
        }

        // Show FileManagerDialog when showFileDialog is true
        if (showFileDialog) {
            FileManagerDialog(
                isDarkMode = isDarkMode,
                onDismiss = { showFileDialog = false },
                onFileSelected = { uri ->
                    // TODO: Handle the selected folder URI
                    showFileDialog = false
                }
            )
        }
    }
}

@Composable
fun LoadingScreen(isDarkMode: Boolean = true) {
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(1000),
        label = "logo_scale"
    )

    // Battery charging animation
    var batteryLevel by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        animate(0f, 1f, animationSpec = tween(6000, easing = LinearEasing)) { value, _ ->
            batteryLevel = value
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AnimatedIridescentBackground(isDarkMode = isDarkMode)

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // PyNoy Logo with vertical battery charging effect
            Box(
                modifier = Modifier.size(280.dp),
                contentAlignment = Alignment.Center
            ) {
                // Background container
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = Color.Transparent,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    // Animated fill background
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(batteryLevel)
                            .background(
                                color = Color.White.copy(alpha = 0.6f),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .align(Alignment.BottomCenter)
                    )
                }

                // PyNoy Logo on top
                Image(
                    painter = painterResource(id = R.drawable.pynoy_logo),
                    contentDescription = "PyNoy Logo",
                    modifier = Modifier
                        .size(260.dp)
                        .scale(scale),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Kamusta, Kodigero!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Copyright section
            Text(
                text = "© Group 17 - BSCS",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun MainScreen(
    onCodeEditorClick: () -> Unit,
    onSettingsClick: () -> Unit,
    isDarkMode: Boolean = true,
    onFileOpen: () -> Unit = {}
) {
    var showHelpDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AnimatedIridescentBackground(isDarkMode = isDarkMode)

        // Top bar: Settings (left) and Help (right)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Settings icon (top-left)
            IconButton(onClick = { onSettingsClick() }) {
                AssetImage(
                    baseName = "Settings_button",
                    contentDescription = "Settings",
                    modifier = Modifier.size(40.dp),
                    contentScale = ContentScale.Crop
                )
            }

            // Help icon (top-right)
            IconButton(onClick = { showHelpDialog = true }) {
                AssetImage(
                    baseName = "Help_button",
                    contentDescription = "Help",
                    modifier = Modifier.size(40.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Title section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                Text(
                    text = "Pynoy",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkMode) Color.White else Color.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "A Filipino-based Programming Language",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isDarkMode) Color.White.copy(alpha = 0.8f) else Color.Black.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
            }

            // Two stacked white buttons with provided images
            AssetImageButton(
                baseName = "Bukas_button",
                contentDescription = "Magbukas ng code",
                onClick = { onFileOpen() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            AssetImageButton(
                baseName = "Code_button",
                contentDescription = "Mag-code",
                onClick = { onCodeEditorClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Copyright section
            Text(
                text = "© Group 17 - BSCS",
                fontSize = 14.sp,
                color = if (isDarkMode) Color.White.copy(alpha = 0.7f) else Color.Black.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Help Dialog
        if (showHelpDialog) {
            HelpDialog(
                isDarkMode = isDarkMode,
                onDismiss = { showHelpDialog = false }
            )
        }
    }
}

@Composable
fun MainButton(
    text: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(100),
        label = "button_scale"
    )

    Card(
        modifier = modifier
            .height(120.dp)
            .scale(scale)
            .clickable {
                isPressed = true
                onClick()
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = BackgroundWhite
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundWhite),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                color = TextBlack,
                textAlign = TextAlign.Center
            )

            // Pop color indicator
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .background(
                        color = color,
                        shape = RoundedCornerShape(6.dp)
                    )
            )
        }
    }
}

@Composable
fun ImageActionButton(
    imageRes: Int,
    contentDescription: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = tween(100),
        label = "image_button_scale"
    )

    Card(
        modifier = modifier
            .scale(scale)
            .clickable {
                isPressed = true
                onClick()
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = BackgroundWhite
        )
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun AssetImage(
    baseName: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    val context = LocalContext.current
    val bitmap = remember(baseName) {
        try {
            // Try common extensions in order
            val candidates = listOf("$baseName.png", "$baseName.webp", "$baseName.jpg", "$baseName.jpeg")
            var bmp: android.graphics.Bitmap? = null
            for (candidate in candidates) {
                try {
                    context.assets.open(candidate).use { input ->
                        bmp = BitmapFactory.decodeStream(input)
                    }
                } catch (_: Exception) { /* try next */ }
                if (bmp != null) break
            }
            bmp
        } catch (e: Exception) {
            null
        }
    }

    if (bitmap != null) {
        val composeBitmap = bitmap.asImageBitmap()
        Image(
            bitmap = composeBitmap,
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale
        )
    }
}

@Composable
fun AssetImageButton(
    baseName: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = tween(100),
        label = "asset_image_button_scale"
    )

    Card(
        modifier = modifier
            .scale(scale)
            .clickable {
                isPressed = true
                onClick()
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = BackgroundWhite
        )
    ) {
        AssetImage(
            baseName = baseName,
            contentDescription = contentDescription,
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun HelpButton(
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(100),
        label = "help_button_scale"
    )

    Card(
        modifier = Modifier
            .size(120.dp)
            .scale(scale)
            .clickable {
                isPressed = true
                onClick()
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = BackgroundWhite
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundWhite),
            contentAlignment = Alignment.Center
        ) {
            // Question mark icon
            Text(
                text = "Tulong",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = TextBlack,
                textAlign = TextAlign.Center
            )

            // Help indicator
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .background(
                        color = Color(0xFF10B981), // Green color for help
                        shape = RoundedCornerShape(6.dp)
                    )
            )
        }
    }
}

@Composable
fun HelpDialog(
    isDarkMode: Boolean,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = if (isDarkMode) Color.Black.copy(alpha = 0.8f) else Color.White.copy(alpha = 0.8f)
            )
            .clickable { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .padding(16.dp)
                .clickable { /* Prevent click from dismissing */ },
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDarkMode) Color(0xFF1F2937) else Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Help Icon
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            color = Color(0xFF10B981),
                            shape = RoundedCornerShape(40.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "?",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Title
                Text(
                    text = "Tulong at Gabay",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkMode) Color.White else Color.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Subtitle
                Text(
                    text = "Mga function at feature ng PyNoy",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isDarkMode) Color.White.copy(alpha = 0.8f) else Color.Black.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Help Content
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    HelpSection(
                        title = "Magbukas ng code",
                        description = "Buksan ang existing Python file para i-edit o tingnan",
                        isDarkMode = isDarkMode
                    )

                    HelpSection(
                        title = "Katakdaan",
                        description = "I-customize ang app settings tulad ng tema, font size, at font style",
                        isDarkMode = isDarkMode
                    )

                    HelpSection(
                        title = "Mag-code",
                        description = "Pumunta sa code editor para magsulat ng bagong Python code",
                        isDarkMode = isDarkMode
                    )

                    HelpSection(
                        title = "Code Editor Features",
                        description = "• Multiple tabs para sa iba't ibang files\n• Undo/Redo functionality\n• Syntax highlighting\n• File save at load\n• Sample code loading\n• PyNoy code execution",
                        isDarkMode = isDarkMode
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Close Button
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onDismiss() },
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF10B981)
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Sara",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HelpSection(
    title: String,
    description: String,
    isDarkMode: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDarkMode) Color(0xFF374151) else Color(0xFFF3F4F6)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDarkMode) Color.White else Color.Black,
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = description,
                fontSize = 14.sp,
                color = if (isDarkMode) Color.White.copy(alpha = 0.8f) else Color.Black.copy(alpha = 0.8f),
                textAlign = TextAlign.Start,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    isDarkMode: Boolean = true,
    onThemeChange: (Boolean) -> Unit = {},
    fontSize: TextUnit = 14.sp,
    onFontSizeChange: (TextUnit) -> Unit = {},
    fontStyle: String = "Monospace",
    onFontStyleChange: (String) -> Unit = {}
) {
    var showAboutDialog by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AnimatedIridescentBackground(isDarkMode = isDarkMode)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(32.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back Button
                Card(
                    modifier = Modifier
                        .size(48.dp)
                        .clickable { onBackClick() },
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "←",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextBlack
                        )
                    }
                }

                Text(
                    text = "Katakdaan",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkMode) Color.White else Color.Black
                )

                // Empty space for balance
                Spacer(modifier = Modifier.width(48.dp))
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Settings Options
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Theme Settings
                ThemeSettingsCard(
                    isDarkMode = isDarkMode,
                    onThemeChange = onThemeChange
                )

                // Font Style Settings
                FontStyleSettingsCard(
                    fontStyle = fontStyle,
                    onFontStyleChange = onFontStyleChange
                )

                // Font Size Settings
                FontSizeSettingsCard(
                    fontSize = fontSize,
                    onFontSizeChange = onFontSizeChange
                )

                // About App
                AboutAppCard(
                    onAboutClick = { showAboutDialog = true }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Copyright section
            Text(
                text = "© Group 17 - BSCS",
                fontSize = 14.sp,
                color = if (isDarkMode) Color.White.copy(alpha = 0.7f) else Color.Black.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            )
        }

        // About Dialog
        if (showAboutDialog) {
            AboutDialog(
                isDarkMode = isDarkMode,
                onDismiss = { showAboutDialog = false }
            )
        }
    }
}

@Composable
fun ThemeSettingsCard(
    isDarkMode: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Tema",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = TextBlack
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Piliin ang kulay ng app",
                fontSize = 14.sp,
                color = Color(0xFF6B7280),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Theme Options
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Dark Mode Option
                ThemeOption(
                    title = "Madilim",
                    isSelected = isDarkMode,
                    onClick = { onThemeChange(true) },
                    modifier = Modifier.weight(1f)
                )

                // Light Mode Option
                ThemeOption(
                    title = "Maliwanag",
                    isSelected = !isDarkMode,
                    onClick = { onThemeChange(false) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun ThemeOption(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF8B5CF6) else Color(0xFFF3F4F6)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else TextBlack,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun FontStyleSettingsCard(
    fontStyle: String,
    onFontStyleChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Font Style",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = TextBlack
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Piliin ang estilo ng font",
                fontSize = 14.sp,
                color = Color(0xFF6B7280),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Font Style Options
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Monospace Option
                FontStyleOption(
                    title = "Monospace",
                    isSelected = fontStyle == "Monospace",
                    onClick = { onFontStyleChange("Monospace") },
                    modifier = Modifier.weight(1f)
                )

                // Sans Serif Option
                FontStyleOption(
                    title = "Sans Serif",
                    isSelected = fontStyle == "Sans Serif",
                    onClick = { onFontStyleChange("Sans Serif") },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun FontStyleOption(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF8B5CF6) else Color(0xFFF3F4F6)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else TextBlack,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun FontSizeSettingsCard(
    fontSize: TextUnit,
    onFontSizeChange: (TextUnit) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Font Size",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = TextBlack
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Piliin ang laki ng font",
                fontSize = 14.sp,
                color = Color(0xFF6B7280),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Font Size Options
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Small Option
                FontSizeOption(
                    title = "Small",
                    size = 12.sp,
                    isSelected = fontSize == 12.sp,
                    onClick = { onFontSizeChange(12.sp) },
                    modifier = Modifier.weight(1f)
                )

                // Medium Option
                FontSizeOption(
                    title = "Medium",
                    size = 14.sp,
                    isSelected = fontSize == 14.sp,
                    onClick = { onFontSizeChange(14.sp) },
                    modifier = Modifier.weight(1f)
                )

                // Large Option
                FontSizeOption(
                    title = "Large",
                    size = 16.sp,
                    isSelected = fontSize == 16.sp,
                    onClick = { onFontSizeChange(16.sp) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun FontSizeOption(
    title: String,
    size: TextUnit,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF8B5CF6) else Color(0xFFF3F4F6)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                fontSize = size,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else TextBlack,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun AboutAppCard(
    onAboutClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onAboutClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Tungkol sa App",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextBlack
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Impormasyon tungkol sa PyNoy",
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280),
                    textAlign = TextAlign.Start
                )
            }

            Text(
                text = ">",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6B7280)
            )
        }
    }
}

@Composable
fun AboutDialog(
    isDarkMode: Boolean,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = if (isDarkMode) Color.Black.copy(alpha = 0.8f) else Color.White.copy(alpha = 0.8f)
            )
            .clickable { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
                .clickable { /* Prevent click from dismissing */ },
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDarkMode) Color(0xFF1F2937) else Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // App Logo
                Image(
                    painter = painterResource(id = R.drawable.pynoy_logo),
                    contentDescription = "PyNoy Logo",
                    modifier = Modifier.size(80.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(16.dp))

                // App Title
                Text(
                    text = "PyNoy",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkMode) Color.White else Color.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                // App Subtitle
                Text(
                    text = "Filipino Programming Language",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isDarkMode) Color.White.copy(alpha = 0.8f) else Color.Black.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Description
                Text(
                    text = "Ang PyNoy ay isang programming language na ginawa para sa mga Filipino developers. " +
                            "Ito ay naglalayong maging madaling gamitin at maintindihan ng mga Pilipino, " +
                            "gamit ang mga salitang Tagalog at Filipino concepts sa programming.",
                    fontSize = 14.sp,
                    color = if (isDarkMode) Color.White.copy(alpha = 0.9f) else Color.Black.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Features
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FeatureItem(
                        text = "• Madaling gamitin para sa mga Filipino",
                        isDarkMode = isDarkMode
                    )
                    FeatureItem(
                        text = "• Tagalog keywords at syntax",
                        isDarkMode = isDarkMode
                    )
                    FeatureItem(
                        text = "• Filipino programming concepts",
                        isDarkMode = isDarkMode
                    )
                    FeatureItem(
                        text = "• Designed para sa Filipino developers",
                        isDarkMode = isDarkMode
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Close Button
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onDismiss() },
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF8B5CF6)
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Sara",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FeatureItem(
    text: String,
    isDarkMode: Boolean
) {
    Text(
        text = text,
        fontSize = 13.sp,
        color = if (isDarkMode) Color.White.copy(alpha = 0.8f) else Color.Black.copy(alpha = 0.8f),
        textAlign = TextAlign.Start,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun FileManagerDialog(
    isDarkMode: Boolean,
    onDismiss: () -> Unit,
    onFileSelected: (String) -> Unit
) {
    val context = LocalContext.current

    // SAF launcher for folder picking
    val folderPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uri ->
        if (uri != null) {
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
            onFileSelected(uri.toString())
        }
        onDismiss()
    }

    // Immediately launch the folder picker when the dialog is shown
    LaunchedEffect(Unit) {
        folderPickerLauncher.launch(null)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = if (isDarkMode) Color.Black.copy(alpha = 0.8f) else Color.White.copy(alpha = 0.8f)
            )
            .clickable { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
                .clickable { /* Prevent click from dismissing */ },
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDarkMode) Color(0xFF1F2937) else Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Pumili ng folder gamit ang file manager...",
                    fontSize = 16.sp,
                    color = if (isDarkMode) Color.White else Color.Black,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onDismiss() },
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDarkMode) Color(0xFF374151) else Color(0xFFF3F4F6)
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Sara",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isDarkMode) Color.White else Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PermissionDeniedDialog(
    isDarkMode: Boolean,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = if (isDarkMode) Color.Black.copy(alpha = 0.9f) else Color.White.copy(alpha = 0.9f)
            )
            .clickable { onDismiss() },
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
                .clickable { /* Prevent click from dismissing */ },
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDarkMode) Color(0xFF1F2937) else Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Permission Required",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDarkMode) Color.White else Color.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Kailangan ng permission para ma-access ang file manager. Pumunta sa Settings para i-enable ang storage permission.",
                    fontSize = 14.sp,
                    color = if (isDarkMode) Color.White.copy(alpha = 0.8f) else Color.Black.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onDismiss() },
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF8B5CF6)
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "OK",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsCard(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextBlack
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280),
                    textAlign = TextAlign.Start
                )
            }

            Text(
                text = ">",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6B7280)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    PyNoyCompilerTheme {
        LoadingScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    PyNoyCompilerTheme {
        MainScreen(
            onCodeEditorClick = { /* Preview only */ },
            onSettingsClick = { /* Preview only */ },
            isDarkMode = true
        )
    }
}

@Composable
fun CodeEditorScreen(
    onBackClick: () -> Unit,
    onSaveFile: () -> Unit,
    onLoadFile: () -> Unit,
    onRunCode: () -> Unit,
    isDarkMode: Boolean = true,
    fontSize: TextUnit = 12.sp,
    fontStyle: String = "Monospace",
    pendingFileUri: Uri? = null,
    onPendingFileHandled: () -> Unit = {}
) {
    val context = LocalContext.current
    var codeText by remember { mutableStateOf("") }
    var showOutput by remember { mutableStateOf(false) }
    var selectedTabIndex by remember { mutableStateOf(0) }
    var tabs by remember { mutableStateOf(listOf(CodeTab("Untitled", ""))) }
    var undoStack by remember { mutableStateOf(listOf<String>()) }
    var redoStack by remember { mutableStateOf(listOf<String>()) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showSaveDialog by remember { mutableStateOf(false) }
    var showLoadDialog by remember { mutableStateOf(false) }
    var fileName by remember { mutableStateOf("") }
    var showDuplicateDialog by remember { mutableStateOf(false) }
    var duplicateFileName by remember { mutableStateOf("") }
    var outputText by remember { mutableStateOf("") }

    // Function to load sample PyNoy code
    fun loadSampleCode() {
        val sampleCode = """// PyNoy Sample Code
// This demonstrates the basic features of PyNoy

// Basic arithmetic operations
a = 10
b = 5
pakita("Basic Arithmetic:")
pakita("a = " + a)
pakita("b = " + b)
pakita("a + b = " + (a + b))
pakita("a - b = " + (a - b))
pakita("a * b = " + (a * b))
pakita("a / b = " + (a / b))

// String operations
pangalan = "Juan"
edad = 25
pakita("")
pakita("String Operations:")
pakita("Pangalan: " + pangalan)
pakita("Edad: " + pangalan)
pakita("Message: " + pangalan + " ay " + edad + " taong gulang")

// Arrays
numbers = [1, 2, 3, 4, 5]
fruits = ["mansanas", "saging", "dalandan"]
pakita("")
pakita("Arrays:")
pakita("Numbers: " + numbers)
pakita("Fruits: " + fruits)

// Variables and expressions
x = 20
y = 4
result = x + y * 2
pakita("")
pakita("Complex Expression:")
pakita("x + y * 2 = " + result)

// Final message
pakita("")
pakita("=== PyNoy Code Execution Complete ===")
pakita("Salamat sa paggamit ng PyNoy!")"""

        // Create a new tab with sample code
        val newTab = CodeTab("Sample Code", sampleCode)
        tabs = tabs + newTab
        selectedTabIndex = tabs.size - 1
    }

    // Function to execute PyNoy code
    fun executePyNoyCode(code: String) {
        try {
            // Simple PyNoy interpreter simulation
            val output = mutableListOf<String>()
            val lines = code.split("\n")
            var lineNumber = 0
            val variables = mutableMapOf<String, Any>()

            output.add("===== Sir eto na po wait lang =====")
            output.add("")

            // Helper function to evaluate expressions
            fun evaluateExpression(expr: String): Any {
                return try {
                    when {
                        expr.matches(Regex("^-?\\d+(\\.\\d+)?$")) -> {
                            // It's a number
                            expr.toDouble()
                        }
                        expr.contains("at") || expr.contains("o") || expr.contains("hindi") -> {
                            // Logical operators
                            evaluateLogicalExpression(expr)
                        }
                        expr.contains("mas malaki") || expr.contains("mas maliit") || 
                        expr.contains("pareho") || expr.contains("hindi pareho") ||
                        expr.contains("mas malaki o pareho") || expr.contains("mas maliit o pareho") -> {
                            // Comparison operators
                            evaluateComparisonExpression(expr)
                        }
                        expr.contains("+") || expr.contains("-") || 
                        expr.contains("*") || expr.contains("/") -> {
                            // It's an arithmetic expression - try to evaluate
                            try {
                                // Simple arithmetic evaluation
                                val cleanExpr = expr.replace(" ", "")
                                when {
                                    cleanExpr.contains("+") -> {
                                        val parts = cleanExpr.split("+")
                                        if (parts.size == 2) {
                                            val left = parts[0].toDoubleOrNull() ?: (variables[parts[0]] as? Double ?: 0.0)
                                            val right = parts[1].toDoubleOrNull() ?: (variables[parts[1]] as? Double ?: 0.0)
                                            left + right
                                        } else expr
                                    }
                                    cleanExpr.contains("-") -> {
                                        val parts = cleanExpr.split("-")
                                        if (parts.size == 2) {
                                            val left = parts[0].toDoubleOrNull() ?: (variables[parts[0]] as? Double ?: 0.0)
                                            val right = parts[1].toDoubleOrNull() ?: (variables[parts[1]] as? Double ?: 0.0)
                                            left - right
                                        } else expr
                                    }
                                    cleanExpr.contains("*") -> {
                                        val parts = cleanExpr.split("*")
                                        if (parts.size == 2) {
                                            val left = parts[0].toDoubleOrNull() ?: (variables[parts[0]] as? Double ?: 0.0)
                                            val right = parts[1].toDoubleOrNull() ?: (variables[parts[1]] as? Double ?: 0.0)
                                            left * right
                                        } else expr
                                    }
                                    cleanExpr.contains("/") -> {
                                        val parts = cleanExpr.split("/")
                                        if (parts.size == 2) {
                                            val left = parts[0].toDoubleOrNull() ?: (variables[parts[0]] as? Double ?: 0.0)
                                            val right = parts[1].toDoubleOrNull() ?: (variables[parts[1]] as? Double ?: 0.0)
                                            if (right != 0.0) left / right else 0.0
                                        } else expr
                                    }
                                    else -> expr
                                }
                            } catch (e: Exception) {
                                expr
                            }
                        }
                        else -> {
                            // Variable or other content
                            variables[expr] ?: expr
                        }
                    }
                } catch (e: Exception) {
                    expr
                }
            }

            // Helper function to evaluate logical expressions
            fun evaluateLogicalExpression(expr: String): Boolean {
                return try {
                    when {
                        expr.contains("at") -> {
                            val parts = expr.split("at")
                            if (parts.size == 2) {
                                val left = evaluateBooleanValue(parts[0].trim())
                                val right = evaluateBooleanValue(parts[1].trim())
                                left && right
                            } else false
                        }
                        expr.contains("o") -> {
                            val parts = expr.split("o")
                            if (parts.size == 2) {
                                val left = evaluateBooleanValue(parts[0].trim())
                                val right = evaluateBooleanValue(parts[1].trim())
                                left || right
                            } else false
                        }
                        expr.startsWith("hindi") -> {
                            val content = expr.substringAfter("hindi").trim()
                            !evaluateBooleanValue(content)
                        }
                        else -> false
                    }
                } catch (e: Exception) {
                    false
                }
            }

            // Helper function to evaluate comparison expressions
            fun evaluateComparisonExpression(expr: String): Boolean {
                return try {
                    when {
                        expr.contains("mas malaki o pareho") -> {
                            val parts = expr.split("mas malaki o pareho")
                            if (parts.size == 2) {
                                val left = evaluateNumericValue(parts[0].trim())
                                val right = evaluateNumericValue(parts[1].trim())
                                left >= right
                            } else false
                        }
                        expr.contains("mas maliit o pareho") -> {
                            val parts = expr.split("mas maliit o pareho")
                            if (parts.size == 2) {
                                val left = evaluateNumericValue(parts[0].trim())
                                val right = evaluateNumericValue(parts[1].trim())
                                left <= right
                            } else false
                        }
                        expr.contains("mas malaki") -> {
                            val parts = expr.split("mas malaki")
                            if (parts.size == 2) {
                                val left = evaluateNumericValue(parts[0].trim())
                                val right = evaluateNumericValue(parts[1].trim())
                                left > right
                            } else false
                        }
                        expr.contains("mas maliit") -> {
                            val parts = expr.split("mas maliit")
                            if (parts.size == 2) {
                                val left = evaluateNumericValue(parts[0].trim())
                                val right = evaluateNumericValue(parts[1].trim())
                                left < right
                            } else false
                        }
                        expr.contains("pareho") -> {
                            val parts = expr.split("pareho")
                            if (parts.size == 2) {
                                val left = evaluateNumericValue(parts[0].trim())
                                val right = evaluateNumericValue(parts[1].trim())
                                left == right
                            } else false
                        }
                        expr.contains("hindi pareho") -> {
                            val parts = expr.split("hindi pareho")
                            if (parts.size == 2) {
                                val left = evaluateNumericValue(parts[0].trim())
                                val right = evaluateNumericValue(parts[1].trim())
                                left != right
                            } else false
                        }
                        else -> false
                    }
                } catch (e: Exception) {
                    false
                }
            }

            // Helper function to evaluate boolean values
            fun evaluateBooleanValue(value: String): Boolean {
                return when {
                    value == "totoo" || value == "true" -> true
                    value == "mali" || value == "false" -> false
                    value.matches(Regex("^-?\\d+(\\.\\d+)?$")) -> value.toDouble() != 0.0
                    else -> {
                        val varValue = variables[value]
                        when (varValue) {
                            is Boolean -> varValue
                            is Number -> varValue.toDouble() != 0.0
                            is String -> varValue.isNotEmpty()
                            else -> false
                        }
                    }
                }
            }

            // Helper function to evaluate numeric values
            fun evaluateNumericValue(value: String): Double {
                return when {
                    value.matches(Regex("^-?\\d+(\\.\\d+)?$")) -> value.toDouble()
                    else -> {
                        val varValue = variables[value]
                        when (varValue) {
                            is Number -> varValue.toDouble()
                            is String -> varValue.toDoubleOrNull() ?: 0.0
                            else -> 0.0
                        }
                    }
                }
            }

            for (line in lines) {
                lineNumber++
                val trimmedLine = line.trim()
                if (trimmedLine.startsWith("//")) continue // Skip comments
                if (trimmedLine.isEmpty()) continue

                // Check for unrecognized capitalized keywords
                val capitalizedKeywords = listOf("Pakita", "Kung", "Sakali", "Kundi", "Ulitin", "Para Ulit", "Habang", "Tigil", "Tuloy", "Def", "Para", "Gawin", "Tapos", "Kundi", "Para", "Sa", "Ng", "Ay", "At", "O", "Ngunit", "Subalit", "Dahil", "Kaya", "Kung", "Kailan", "Saan", "Paano", "Bakit", "Sino", "Ano", "Alin", "Ilan", "Magkano", "Kailan", "Saan", "Paano", "Bakit", "Sino", "Ano", "Alin", "Ilan", "Magkano")
                
                // Check if line starts with any capitalized keyword that's not "pakita"
                val unrecognizedKeyword = capitalizedKeywords.find { keyword ->
                    trimmedLine.startsWith("$keyword(") || trimmedLine.startsWith("$keyword ")
                }
                
                if (unrecognizedKeyword != null) {
                    output.add("MAY MALI: Hindi makilala ang word na \"$unrecognizedKeyword\" sa line $lineNumber")
                    continue
                }
                
                // Also check for any other capitalized words that might be syntax errors
                val words = trimmedLine.split("\\s+".toRegex())
                val unrecognizedWord = words.find { word ->
                    word.length > 1 && 
                    word[0].isUpperCase() && 
                    word[1].isLowerCase() && 
                    !word.matches(Regex("^[a-zA-Z_][a-zA-Z0-9_]*$")) &&
                    !word.startsWith("\"") &&
                    !word.endsWith("\"") &&
                    !word.contains("=") &&
                    !word.contains("(") &&
                    !word.contains(")") &&
                    !word.contains("+") &&
                    !word.contains("-") &&
                    !word.contains("*") &&
                    !word.contains("/") &&
                    !word.matches(Regex("^-?\\d+(\\.\\d+)?$"))
                }
                
                if (unrecognizedWord != null && !trimmedLine.startsWith("pakita(")) {
                    output.add("MAY MALI: Hindi makilala ang word na \"$unrecognizedWord\" sa line $lineNumber")
                    continue
                }

                // Check for spaces in syntax elements (case-sensitive)
                val syntaxElements = listOf(
                    "pakita", "totoo", "mali", "true", "false",
                    "at", "o", "hindi", "mas malaki", "mas maliit", 
                    "pareho", "hindi pareho", "mas malaki o pareho", "mas maliit o pareho",
                    "kung", "sakali", "kundi", "ulitin", "para ulit", "habang",
                    "tigil", "tuloy", "def"
                )
                
                // Check for spaces in pakita function
                if (trimmedLine.contains("pakita") && !trimmedLine.startsWith("pakita(")) {
                    val pakitaIndex = trimmedLine.indexOf("pakita")
                    if (pakitaIndex > 0 && trimmedLine[pakitaIndex - 1] != ' ') {
                        output.add("MAY MALI: tanggalin ang espasyo sa line $lineNumber")
                        continue
                    }
                    if (pakitaIndex + 5 < trimmedLine.length && trimmedLine[pakitaIndex + 5] != '(') {
                        output.add("MAY MALI: tanggalin ang espasyo sa line $lineNumber")
                        continue
                    }
                }
                
                // Check for spaces in boolean values
                if (trimmedLine.contains("totoo") || trimmedLine.contains("mali")) {
                    val words = trimmedLine.split("\\s+".toRegex())
                    val hasSpacedBoolean = words.any { word ->
                        word.contains("totoo") && word != "totoo" ||
                        word.contains("mali") && word != "mali" ||
                        word.contains("true") && word != "true" ||
                        word.contains("false") && word != "false"
                    }
                    if (hasSpacedBoolean) {
                        output.add("MAY MALI: tanggalin ang espasyo sa line $lineNumber")
                        continue
                    }
                }
                
                // Check for spaces in logical operators
                if (trimmedLine.contains("at") || trimmedLine.contains("o") || trimmedLine.contains("hindi")) {
                    val words = trimmedLine.split("\\s+".toRegex())
                    val hasSpacedOperator = words.any { word ->
                        word.contains("at") && word != "at" ||
                        word.contains("o") && word != "o" ||
                        word.contains("hindi") && word != "hindi"
                    }
                    if (hasSpacedOperator) {
                        output.add("MAY MALI: tanggalin ang espasyo sa line $lineNumber")
                        continue
                    }
                }
                
                // Check for spaces in comparison operators
                val comparisonOperators = listOf("mas malaki", "mas maliit", "pareho", "hindi pareho", "mas malaki o pareho", "mas maliit o pareho")
                for (operator in comparisonOperators) {
                    if (trimmedLine.contains(operator)) {
                        val words = trimmedLine.split("\\s+".toRegex())
                        val hasSpacedComparison = words.any { word ->
                            word.contains(operator) && word != operator
                        }
                        if (hasSpacedComparison) {
                            output.add("MAY MALI: tanggalin ang espasyo sa line $lineNumber")
                            continue
                        }
                    }
                }
                
                // Check for spaces in control structures
                val controlKeywords = listOf("kung", "sakali", "kundi", "ulitin", "para ulit", "habang", "tigil", "tuloy", "def")
                for (keyword in controlKeywords) {
                    if (trimmedLine.contains(keyword)) {
                        val words = trimmedLine.split("\\s+".toRegex())
                        val hasSpacedKeyword = words.any { word ->
                            word.contains(keyword) && word != keyword
                        }
                        if (hasSpacedKeyword) {
                            output.add("MAY MALI: tanggalin ang espasyo sa line $lineNumber")
                            continue
                        }
                    }
                }

                try {
                    when {
                        trimmedLine.startsWith("pakita(") -> {
                            // Check for missing closing parenthesis
                            if (!trimmedLine.contains(")")) {
                                output.add("MAY MALI: Kulang ng \")\" sa line $lineNumber")
                                continue
                            }
                            
                            // Extract content inside pakita()
                            val content =
                                trimmedLine.substringAfter("pakita(").substringBeforeLast(")")
                            
                            // Check for unclosed quotes in pakita
                            if (content.startsWith("\"") && !content.endsWith("\"")) {
                                output.add("MAY MALI: Kulang ng \" sa line $lineNumber")
                                continue
                            }
                            
                            if (content.startsWith("\"") && content.endsWith("\"")) {
                                // String literal
                                val message = content.substring(1, content.length - 1)
                                output.add("OUTPUT: $message")
                            } else {
                                // Variable, number, or expression
                                val result = evaluateExpression(content)
                                output.add("OUTPUT: $result")
                            }
                        }

                        trimmedLine.contains("=") -> {
                            // Assignment
                            val parts = trimmedLine.split("=", limit = 2)
                            val varName = parts[0].trim()
                            val value = parts[1].trim()
                            
                            // Check for unclosed quotes in assignment
                            if (value.startsWith("\"") && !value.endsWith("\"")) {
                                output.add("MAY MALI: Kulang ng \" sa line $lineNumber")
                                continue
                            }
                            
                            // Evaluate the value and store it
                            val evaluatedValue = evaluateExpression(value)
                            variables[varName] = evaluatedValue
                            
                            when {
                                evaluatedValue is Double -> {
                                    output.add("ASSIGN: Variable '$varName' = $evaluatedValue")
                                }
                                evaluatedValue is String -> {
                                    output.add("ASSIGN: Variable '$varName' = \"$evaluatedValue\"")
                                }
                                else -> {
                                    output.add("ASSIGN: Variable '$varName' = $evaluatedValue")
                                }
                            }
                        }

                        trimmedLine.contains("+") || trimmedLine.contains("-") ||
                                trimmedLine.contains("*") || trimmedLine.contains("/") -> {
                            // Arithmetic operation
                            val result = evaluateExpression(trimmedLine)
                            output.add("CALC: $trimmedLine = $result")
                        }

                        trimmedLine.contains("at") || trimmedLine.contains("o") || trimmedLine.contains("hindi") -> {
                            // Logical operation
                            val result = evaluateExpression(trimmedLine)
                            val booleanResult = if (result is Boolean) result else false
                            output.add("LOGIC: $trimmedLine = ${if (booleanResult) "totoo" else "mali"}")
                        }

                        trimmedLine.contains("mas malaki") || trimmedLine.contains("mas maliit") || 
                        trimmedLine.contains("pareho") || trimmedLine.contains("hindi pareho") ||
                        trimmedLine.contains("mas malaki o pareho") || trimmedLine.contains("mas maliit o pareho") -> {
                            // Comparison operation
                            val result = evaluateExpression(trimmedLine)
                            val booleanResult = if (result is Boolean) result else false
                            output.add("COMPARE: $trimmedLine = ${if (booleanResult) "totoo" else "mali"}")
                        }

                        trimmedLine.startsWith("kung") -> {
                            // If statement
                            if (!trimmedLine.contains(":")) {
                                output.add("MAY MALI: Kulang ng \":\" sa line $lineNumber")
                                continue
                            }
                            val condition = trimmedLine.substringAfter("kung").substringBefore(":").trim()
                            val result = evaluateExpression(condition)
                            val booleanResult = if (result is Boolean) result else false
                            output.add("IF: $condition = ${if (booleanResult) "totoo" else "mali"}")
                        }

                        trimmedLine.startsWith("sakali") -> {
                            // Elif statement
                            if (!trimmedLine.contains(":")) {
                                output.add("MAY MALI: Kulang ng \":\" sa line $lineNumber")
                                continue
                            }
                            val condition = trimmedLine.substringAfter("sakali").substringBefore(":").trim()
                            val result = evaluateExpression(condition)
                            val booleanResult = if (result is Boolean) result else false
                            output.add("ELIF: $condition = ${if (booleanResult) "totoo" else "mali"}")
                        }

                        trimmedLine.startsWith("kundi") -> {
                            // Else statement
                            if (!trimmedLine.contains(":")) {
                                output.add("MAY MALI: Kulang ng \":\" sa line $lineNumber")
                                continue
                            }
                            output.add("ELSE: Executing else block")
                        }

                        trimmedLine.startsWith("ulitin") -> {
                            // Loop statement
                            if (!trimmedLine.contains(":")) {
                                output.add("MAY MALI: Kulang ng \":\" sa line $lineNumber")
                                continue
                            }
                            val condition = trimmedLine.substringAfter("ulitin").substringBefore(":").trim()
                            output.add("LOOP: Starting loop with condition: $condition")
                        }

                        trimmedLine.startsWith("para ulit") -> {
                            // For loop statement
                            if (!trimmedLine.contains(":")) {
                                output.add("MAY MALI: Kulang ng \":\" sa line $lineNumber")
                                continue
                            }
                            val condition = trimmedLine.substringAfter("para ulit").substringBefore(":").trim()
                            output.add("FOR LOOP: Starting for loop with condition: $condition")
                        }

                        trimmedLine.startsWith("habang") -> {
                            // While loop statement
                            if (!trimmedLine.contains(":")) {
                                output.add("MAY MALI: Kulang ng \":\" sa line $lineNumber")
                                continue
                            }
                            val condition = trimmedLine.substringAfter("habang").substringBefore(":").trim()
                            val result = evaluateExpression(condition)
                            val booleanResult = if (result is Boolean) result else false
                            output.add("WHILE: $condition = ${if (booleanResult) "totoo" else "mali"}")
                        }

                        trimmedLine.startsWith("tigil") -> {
                            // Break statement
                            output.add("BREAK: Stopping loop execution")
                        }

                        trimmedLine.startsWith("tuloy") -> {
                            // Continue statement
                            output.add("CONTINUE: Skipping to next iteration")
                        }

                        trimmedLine.startsWith("def") -> {
                            // Function definition
                            if (!trimmedLine.contains(":")) {
                                output.add("MAY MALI: Kulang ng \":\" sa line $lineNumber")
                                continue
                            }
                            val functionName = trimmedLine.substringAfter("def").substringBefore(":").trim()
                            if (functionName.isEmpty()) {
                                output.add("MAY MALI: Kulang ng pangalan ng function sa line $lineNumber")
                                continue
                            }
                            output.add("FUNCTION: Defining function '$functionName'")
                        }

                        else -> {
                            output.add("EXEC: $trimmedLine")
                        }
                    }
                } catch (e: Exception) {
                    // Enhanced error messages in Tagalog
                    val errorMsg = when {
                        e.message?.contains("IndexOutOfBounds") == true -> "MAY MALI: Hindi tama yung syntax sa line $lineNumber"
                        e.message?.contains("NumberFormatException") == true -> "MAY MALI: Hindi valid na number sa line $lineNumber"
                        else -> "MAY MALI: Hindi maintindihan yung code sa line $lineNumber"
                    }
                    output.add(errorMsg)
                }
            }

            output.add("")
            output.add("===== Ser, Tapos na po! =====")

            if (output.isNotEmpty()) {
                outputText = output.joinToString("\n")
                showOutput = true
            } else {
                outputText = "Walang output na na-generate."
                showOutput = true
            }

        } catch (e: Exception) {
            outputText = "ERROR: ${e.message}"
            showOutput = true
        }
    }

    // Handle pending file from main screen
    LaunchedEffect(pendingFileUri) {
        pendingFileUri?.let { uri ->
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val reader = BufferedReader(InputStreamReader(inputStream))
                val content = reader.readText()
                reader.close()
                inputStream?.close()

                val fileName = uri.lastPathSegment ?: "Titulo"

                // Check if file is already open
                val isAlreadyOpen = tabs.any { tab ->
                    tab.filePath == uri.toString() || tab.name == fileName
                }

                if (isAlreadyOpen) {
                    duplicateFileName = fileName
                    showDuplicateDialog = true
                } else {
                    // Create new tab with loaded content
                    val newTab = CodeTab(fileName, content, uri.toString())
                    tabs = tabs + newTab
                    selectedTabIndex = tabs.size - 1
                }

                onPendingFileHandled()

            } catch (e: Exception) {
                showErrorDialog = true
                errorMessage = "Di mabuksan yung file na: ${e.message}"
                onPendingFileHandled()
            }
        }
    }

    // Preferences for persisting default PyNoy folder tree URI
    val prefs =
        remember(context) { context.getSharedPreferences("pynoy_prefs", Context.MODE_PRIVATE) }
    var savedTreeUriString by remember { mutableStateOf(prefs.getString("pynoyTreeUri", null)) }

    // Launcher: pick a specific file with initial directory
    val openDocLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val selectedUri = result.data?.data ?: return@rememberLauncherForActivityResult
        try {
            val inputStream = context.contentResolver.openInputStream(selectedUri)
            val reader = BufferedReader(InputStreamReader(inputStream))
            val content = reader.readText()
            reader.close()
            inputStream?.close()

            // Validate extension
            val nameGuess = selectedUri.path ?: ""
            if (!nameGuess.endsWith(".py")) {
                showErrorDialog = true
                errorMessage = "Python(py) na titulo lamang ang nabubuksan."
                return@rememberLauncherForActivityResult
            }

            val displayName = nameGuess.substringAfterLast('/')

            // Check if file is already open
            val isAlreadyOpen = tabs.any { tab ->
                tab.filePath == selectedUri.toString() || tab.name == displayName
            }

            if (isAlreadyOpen) {
                duplicateFileName = displayName
                showDuplicateDialog = true
            } else {
                // Create new tab with loaded content
                val newTab = CodeTab(displayName, content, selectedUri.toString())
                tabs = tabs + newTab
                selectedTabIndex = tabs.size - 1
            }
        } catch (e: Exception) {
            showErrorDialog = true
            errorMessage = "Error loading file: ${e.message}"
        }
    }

    // Launcher: pick the PyNoy folder once and persist
    val openTreeLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uri ->
        if (uri != null) {
            // Persist permissions and save
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
            prefs.edit().putString("pynoyTreeUri", uri.toString()).apply()
            savedTreeUriString = uri.toString()
            // Immediately open file picker rooted at this folder
            fun buildOpenIntent(tree: Uri): Intent {
                val docId = DocumentsContract.getTreeDocumentId(tree)
                val initial = DocumentsContract.buildDocumentUriUsingTree(tree, docId)
                return Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "*/*"
                    putExtra(DocumentsContract.EXTRA_INITIAL_URI, initial)
                    putExtra(
                        Intent.EXTRA_MIME_TYPES,
                        arrayOf("text/x-python", "application/x-python", "text/plain")
                    )
                }
            }
            openDocLauncher.launch(buildOpenIntent(uri))
        }
    }

    // Save file function
    fun saveCurrentFile() {
        val currentTab = tabs.getOrNull(selectedTabIndex)
        if (currentTab != null) {
            // Show save dialog to get filename
            fileName =
                if (currentTab.name == "Titulo") "" else currentTab.name.replace(".py", "")
            showSaveDialog = true
        }
    }

    // Save file launcher
    val saveFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val uri = result.data?.data
        if (uri != null) {
            try {
                val currentTab = tabs.getOrNull(selectedTabIndex)
                if (currentTab != null) {
                    // Ensure filename ends with .py
                    val finalFileName =
                        if (fileName.endsWith(".py")) fileName else "$fileName.py"

                    // Get output stream from the selected URI
                    context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                        outputStream.write(currentTab.content.toByteArray())
                    }

                    // Update tab with file path
                    tabs = tabs.mapIndexed { index, tab ->
                        if (index == selectedTabIndex) tab.copy(
                            name = finalFileName,
                            filePath = uri.toString()
                        ) else tab
                    }
                }
            } catch (e: Exception) {
                showErrorDialog = true
                errorMessage = "Hindi ma-save ang file!: ${e.message}"
            }
        }
        showSaveDialog = false
    }

    // Actual save function with filename
    fun performSave(filename: String) {
        fileName = filename
        // Create save intent
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/x-python"
            putExtra(
                Intent.EXTRA_TITLE,
                if (filename.endsWith(".py")) filename else "$filename.py"
            )
        }
        saveFileLauncher.launch(intent)
    }

    // Load file function
    fun loadFile() {
        showLoadDialog = true
    }

    fun performLoad() {
        // If we have a saved tree, open the file dialog starting there; else, ask user to pick PyNoy folder once
        val tree = savedTreeUriString?.let { Uri.parse(it) }
        if (tree != null) {
            val docId = DocumentsContract.getTreeDocumentId(tree)
            val initial = DocumentsContract.buildDocumentUriUsingTree(tree, docId)
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "*/*"
                putExtra(DocumentsContract.EXTRA_INITIAL_URI, initial)
                putExtra(
                    Intent.EXTRA_MIME_TYPES,
                    arrayOf("text/x-python", "application/x-python", "text/plain")
                )
            }
            openDocLauncher.launch(intent)
        } else {
            // Suggest the expected PyNoy folder; user can navigate/create if needed
            openTreeLauncher.launch(null)
        }
        showLoadDialog = false
    }

    // Composable functions
    @Composable
    fun ActionButton(
        text: String,
        onClick: () -> Unit,
        backgroundColor: Color,
        textColor: Color
    ) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = backgroundColor
            ),
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier.height(38.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = text,
                color = textColor,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }

    @Composable
    fun TopNavigationBar(
        onBackClick: () -> Unit,
        onLoadSample: () -> Unit
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF2E1B47))
        ) {
            // Main navigation row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left side - Back button
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(8.dp)
                            )
                    ) {
                        Text(
                            text = "←",
                            color = Color(0xFF2E1B47),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "Mag edit dito",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Right side - Empty for now (Run button moved to floating action button)
                Spacer(modifier = Modifier.width(0.dp))
            }

            // Bottom border
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.White.copy(alpha = 0.1f))
            )
        }
    }

    @Composable
    fun RunButton(
        onClick: () -> Unit
    ) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFEF4444) // Red
            ),
            shape = RoundedCornerShape(6.dp),
            modifier = Modifier.height(38.dp),
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "Run",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }

    @Composable
    fun TabBar(
        tabs: List<CodeTab>,
        selectedTabIndex: Int,
        onTabSelected: (Int) -> Unit,
        onTabClosed: (Int) -> Unit,
        onNewTab: () -> Unit
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF2E1B47))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Tab list
            LazyRow(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(tabs.size) { index ->
                    val tab = tabs[index]
                    val isSelected = index == selectedTabIndex

                    Card(
                        modifier = Modifier
                            .clickable { onTabSelected(index) },
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = if (isSelected) 4.dp else 2.dp
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) Color.White else Color(
                                0xFF4C1D95
                            )
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(
                                horizontal = 12.dp,
                                vertical = 8.dp
                            ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = tab.name,
                                fontSize = 12.sp,
                                color = if (isSelected) Color(0xFF2E1B47) else Color.White,
                                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
                            )

                            if (tabs.size > 1) {
                                Spacer(modifier = Modifier.width(8.dp))
                                IconButton(
                                    onClick = { onTabClosed(index) },
                                    modifier = Modifier.size(16.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Close tab",
                                        tint = if (isSelected) Color(0xFF2E1B47) else Color.White,
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // New tab button
            Card(
                modifier = Modifier
                    .clickable { onNewTab() },
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF4C1D95)
                )
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "New tab",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
 
    @Composable
    fun ExpandableFloatingActionButton(
        onRunCode: () -> Unit,
        onSaveFile: () -> Unit,
        onLoadFile: () -> Unit,
        onUndo: () -> Unit,
        onRedo: () -> Unit,
        canUndo: Boolean = false,
        canRedo: Boolean = false
    ) {
        var isExpanded by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .navigationBarsPadding()
                .padding(16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            // Expanded buttons
            if (isExpanded) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    // Top row - Run, Save and Load buttons
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        // Run Button (Bigger)
                        FloatingActionButton(
                            onClick = onRunCode,
                            modifier = Modifier.size(72.dp),
                            containerColor = Color(0xFFEF4444),
                            contentColor = Color.White,
                            elevation = FloatingActionButtonDefaults.elevation(
                                defaultElevation = 6.dp,
                                pressedElevation = 12.dp
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Run Code",
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        // Save Button (Green text button)
                        Button(
                            onClick = onSaveFile,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF10B981) // Green
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .width(80.dp)
                                .height(48.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "Save",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        // Load Button (White text button)
                        Button(
                            onClick = onLoadFile,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .width(80.dp)
                                .height(48.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "Load",
                                color = Color(0xFF2E1B47),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    // Bottom row - Undo, Redo, and X buttons (horizontal)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Undo Button
                        FloatingActionButton(
                            onClick = {
                                if (canUndo) onUndo()
                            },
                            modifier = Modifier.size(48.dp),
                            containerColor = if (canUndo) Color.White else Color(0xFFE5E7EB),
                            contentColor = if (canUndo) Color(0xFF2E1B47) else Color(0xFF9CA3AF),
                            elevation = FloatingActionButtonDefaults.elevation(
                                defaultElevation = if (canUndo) 6.dp else 2.dp,
                                pressedElevation = if (canUndo) 12.dp else 4.dp
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Undo,
                                contentDescription = "Undo",
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Redo Button
                        FloatingActionButton(
                            onClick = {
                                if (canRedo) onRedo()
                            },
                            modifier = Modifier.size(48.dp),
                            containerColor = if (canRedo) Color.White else Color(0xFFE5E7EB),
                            contentColor = if (canRedo) Color(0xFF2E1B47) else Color(0xFF9CA3AF),
                            elevation = FloatingActionButtonDefaults.elevation(
                                defaultElevation = if (canRedo) 6.dp else 2.dp,
                                pressedElevation = if (canRedo) 12.dp else 4.dp
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Redo,
                                contentDescription = "Redo",
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Collapse (Close) Button
                        FloatingActionButton(
                            onClick = { isExpanded = false },
                            modifier = Modifier.size(48.dp),
                            containerColor = Color.White,
                            contentColor = Color(0xFF2E1B47),
                            elevation = FloatingActionButtonDefaults.elevation(
                                defaultElevation = 6.dp,
                                pressedElevation = 12.dp
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Collapse",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            // Main white circle button
            FloatingActionButton(
                onClick = { isExpanded = !isExpanded },
                modifier = Modifier.size(48.dp),
                containerColor = Color.White,
                contentColor = Color(0xFF2E1B47),
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 12.dp
                )
            ) {
                Icon(
                    imageVector = if (isExpanded) Icons.Default.Close else Icons.Default.Add,
                    contentDescription = if (isExpanded) "Close" else "Expand",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }

    @Composable
    fun CodeEditor(
        codeText: String,
        onCodeChange: (String) -> Unit,
        modifier: Modifier = Modifier,
        isDarkMode: Boolean = true,
        fontSize: TextUnit = 14.sp,
        fontStyle: String = "Monospace"
    ) {
        val lines = codeText.split("\n")
        val lineHeight = fontSize * 1.5f

        Row(
            modifier = modifier
                .background(if (isDarkMode) Color.Black else Color.White)
                .border(2.dp, if (isDarkMode) Color.White else Color(0xFF2E1B47))
        ) {
            // Line Numbers
            Column(
                modifier = Modifier
                    .width(50.dp)
                    .background(Color(0xFFF8F9FA))
                    .border(1.dp, Color(0xFFE9ECEF))
                    .padding(vertical = 12.dp)
            ) {
                // Always show at least one line number
                val lineCount = if (lines.size > 0) lines.size else 1
                repeat(lineCount) { index ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(lineHeight.value.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${index + 1}",
                            fontSize = fontSize, // Same size as code text
                            color = Color(0xFF6B7280), // Gray text
                            lineHeight = lineHeight,
                            fontFamily = if (fontStyle == "Monospace") androidx.compose.ui.text.font.FontFamily.Monospace else androidx.compose.ui.text.font.FontFamily.Default
                        )
                    }
                }
            }
            
            // Vertical separator line
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight()
                    .background(Color(0xFFE9ECEF))
            )

            // Code Text Area
            BasicTextField(
                value = codeText,
                onValueChange = onCodeChange,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 16.dp),
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = fontSize,
                    color = if (isDarkMode) Color.White else Color.Black,
                    fontFamily = if (fontStyle == "Monospace") androidx.compose.ui.text.font.FontFamily.Monospace else androidx.compose.ui.text.font.FontFamily.Default,
                    lineHeight = lineHeight
                ),
                cursorBrush = SolidColor(if (isDarkMode) Color.White else Color.Black),
                decorationBox = { innerTextField ->
                    Box {
                        innerTextField()
                        if (codeText.isEmpty()) {
                            Text(
                                text = "// PyNoy Code Template\n// Magsimula na mag code dito...",
                                fontSize = fontSize,
                                color = if (isDarkMode) Color(0xFF9CA3AF) else Color(
                                    0xFF6B7280
                                ), // Adaptive placeholder color
                                fontFamily = if (fontStyle == "Monospace") androidx.compose.ui.text.font.FontFamily.Monospace else androidx.compose.ui.text.font.FontFamily.Default,
                                lineHeight = lineHeight
                            )
                        }
                    }
                }
            )
        }
    }

    @Composable
    fun OutputScreen(
        onBackClick: () -> Unit,
        outputText: String,
        fontSize: TextUnit = 14.sp,
        fontStyle: String = "Monospace"
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            AnimatedIridescentBackground(isDarkMode = isDarkMode)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Back Button
                    Card(
                        modifier = Modifier
                            .size(48.dp)
                            .clickable { onBackClick() },
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "←",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextBlack
                            )
                        }
                    }

                    Text(
                        text = "Output",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDarkMode) Color.White else Color.Black
                    )

                    // Empty space for balance
                    Spacer(modifier = Modifier.width(48.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Output Content
                Card(
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    BasicTextField(
                        value = outputText,
                        onValueChange = { /* Read-only */ },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontSize = fontSize,
                            color = Color.Black,
                            fontFamily = if (fontStyle == "Monospace") androidx.compose.ui.text.font.FontFamily.Monospace else androidx.compose.ui.text.font.FontFamily.Default,
                            lineHeight = fontSize * 1.5f
                        ),
                        readOnly = true,
                        decorationBox = { innerTextField ->
                            Box {
                                innerTextField()
                                if (outputText.isEmpty()) {
                                    Text(
                                        text = "No output available...",
                                        fontSize = fontSize,
                                        color = Color(0xFF9CA3AF),
                                        fontFamily = if (fontStyle == "Monospace") androidx.compose.ui.text.font.FontFamily.Monospace else androidx.compose.ui.text.font.FontFamily.Default,
                                        lineHeight = fontSize * 1.5f
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }
    }

    // Main content
    if (showOutput) {
        BackHandler { showOutput = false }
        OutputScreen(
            onBackClick = { showOutput = false },
            outputText = outputText,
            fontSize = fontSize,
            fontStyle = fontStyle
        )
    } else {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
                            // Navigation Bar
                TopNavigationBar(
                    onBackClick = onBackClick,
                    onLoadSample = { loadSampleCode() }
                )

            // Tab Bar
            TabBar(
                tabs = tabs,
                selectedTabIndex = selectedTabIndex,
                onTabSelected = { index ->
                    // Clear undo/redo stacks when switching tabs
                    undoStack = emptyList()
                    redoStack = emptyList()
                    selectedTabIndex = index
                },
                onTabClosed = { index ->
                    if (tabs.size > 1) {
                        tabs = tabs.filterIndexed { i, _ -> i != index }
                        if (selectedTabIndex >= tabs.size) {
                            selectedTabIndex = tabs.size - 1
                        }
                    }
                },
                onNewTab = {
                    tabs = tabs + CodeTab("Titulo", "")
                    selectedTabIndex = tabs.size - 1
                }
            )

            // Code Editor
            CodeEditor(
                codeText = tabs.getOrNull(selectedTabIndex)?.content ?: "",
                onCodeChange = { newText ->
                    // Update tab content first
                    val currentText = tabs.getOrNull(selectedTabIndex)?.content ?: ""
                    tabs = tabs.mapIndexed { index, tab ->
                        if (index == selectedTabIndex) tab.copy(content = newText) else tab
                    }

                    // Save current state for undo (only if content actually changed and not empty)
                    if (currentText != newText && currentText.isNotEmpty() && newText.isNotEmpty()) {
                        // Limit undo stack size to prevent memory issues
                        if (undoStack.size < 50) {
                            undoStack = undoStack + currentText
                        } else {
                            // Remove oldest entry and add new one
                            undoStack = undoStack.drop(1) + currentText
                        }
                        redoStack = emptyList() // Clear redo stack when new change is made
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                isDarkMode = isDarkMode,
                fontSize = fontSize,
                fontStyle = fontStyle
            )
        }

                    // Floating Action Button - Bottom Right (Above everything)
            ExpandableFloatingActionButton(
                onRunCode = {
                    // Execute the PyNoy code and show real output
                    val currentTab = tabs.getOrNull(selectedTabIndex)
                    if (currentTab != null) {
                        val code = currentTab.content
                        if (code.isNotEmpty()) {
                            executePyNoyCode(code)
                        } else {
                            showErrorDialog = true
                            errorMessage = "Walang code na pwedeng i-execute. Magsulat muna ng code."
                        }
                    }
                },
                onSaveFile = { saveCurrentFile() },
            onLoadFile = { loadFile() },
            onUndo = {
                if (undoStack.isNotEmpty()) {
                    val previousText = undoStack.last()
                    redoStack =
                        redoStack + (tabs.getOrNull(selectedTabIndex)?.content ?: "")
                    undoStack = undoStack.dropLast(1)
                    tabs = tabs.mapIndexed { index, tab ->
                        if (index == selectedTabIndex) tab.copy(content = previousText) else tab
                    }
                }
            },
            onRedo = {
                if (redoStack.isNotEmpty()) {
                    val nextText = redoStack.last()
                    undoStack =
                        undoStack + (tabs.getOrNull(selectedTabIndex)?.content ?: "")
                    redoStack = redoStack.dropLast(1)
                    tabs = tabs.mapIndexed { index, tab ->
                        if (index == selectedTabIndex) tab.copy(content = nextText) else tab
                    }
                }
            },
            canUndo = undoStack.isNotEmpty(),
            canRedo = redoStack.isNotEmpty()
        )
    }

    // Error Dialog
        if (showErrorDialog) {
            AlertDialog(
                onDismissRequest = { showErrorDialog = false },
                title = { Text("Pagkakamali") },
                text = { Text(errorMessage) },
                confirmButton = {
                    TextButton(onClick = { showErrorDialog = false }) {
                        Text("OK")
                    }
                }
            )
        }

        // Save Dialog
        if (showSaveDialog) {
            var tempFileName by remember { mutableStateOf(fileName) }
            AlertDialog(
                onDismissRequest = { showSaveDialog = false },
                title = { Text("I-adya ang file") },
                text = {
                    Column {
                        Text("Pangalan ng file:")
                        Spacer(modifier = Modifier.height(8.dp))
                        BasicTextField(
                            value = tempFileName,
                            onValueChange = { tempFileName = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF3F4F6))
                                .padding(12.dp),
                            textStyle = androidx.compose.ui.text.TextStyle(
                                fontSize = 16.sp,
                                color = Color.Black
                            ),
                            decorationBox = { innerTextField ->
                                Box {
                                    innerTextField()
                                    if (tempFileName.isEmpty()) {
                                        Text(
                                            text = "maglagay ng pangalan ng file (e.g., my_script)",
                                            fontSize = 16.sp,
                                            color = Color(0xFF9CA3AF)
                                        )
                                    }
                                }
                            }
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (tempFileName.isNotEmpty()) {
                                performSave(tempFileName)
                            }
                        }
                    ) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showSaveDialog = false }) {
                        Text("Sara")
                    }
                }
            )
        }

        // Load Dialog
        if (showLoadDialog) {
            AlertDialog(
                onDismissRequest = { showLoadDialog = false },
                title = { Text("Magbukas ng file:") },
                text = { Text("Pumili ng file na bubuksan:") },
                confirmButton = {
                    TextButton(onClick = { performLoad() }) {
                        Text("Maghanap")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showLoadDialog = false }) {
                        Text("Sara")
                    }
                }
            )
        }

        // Duplicate File Dialog
        if (showDuplicateDialog) {
            AlertDialog(
                onDismissRequest = { showDuplicateDialog = false },
                title = { Text("Bukas na ang file") },
                text = { Text("Ang file na '$duplicateFileName' ay bukas sa kabilang tab.") },
                confirmButton = {
                    TextButton(onClick = { showDuplicateDialog = false }) {
                        Text("OK")
                    }
                }
            )
        }
    }

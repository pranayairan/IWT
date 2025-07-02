import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.Navigator
import com.binarybricks.iwt.R
import com.binarybricks.iwt.data.model.WorkoutLog
import com.binarybricks.iwt.di.AppViewModelProvider
import com.binarybricks.iwt.ui.screens.home.HomeViewModel
import com.binarybricks.iwt.ui.theme.IWTTheme
import com.binarybricks.iwt.ui.preview.PreviewWithNavController
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Permission handling
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            viewModel.onPermissionResult(isGranted)
            if (isGranted && uiState.selectedPresetId != null) {
                navController.navigate("workout/${uiState.selectedPresetId}")
            }
        }
    )

    // Check initial permission status
    LaunchedEffect(Unit) {
        val isGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACTIVITY_RECOGNITION
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true // No runtime permission needed before Android Q
        }
        viewModel.onPermissionResult(isGranted)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAF8))
    ) {
        // Header Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF8FAF8))
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            // Settings icon on the left
            Icon(
                painter = painterResource(id = R.drawable.vector_0_1),
                contentDescription = "Settings",
                tint = Color(0xFF0D1C0D),
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(24.dp)
                    .clickable { navController.navigate("settings") }
            )

            // Centered title with icon
            Row(
                modifier = Modifier.align(Alignment.Center),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Interval Walk",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D1C0D)
                )
            }

            // Person icon on the right
            Icon(
                painter = painterResource(id = R.drawable.walking_man_sideview),
                contentDescription = "Person",
                tint = Color(0xFF0D1C0D),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(24.dp)
            )
        }

        // Content Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            // Title
            Text(
                text = "Ready for your walk?",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0D1C0D),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            )

            // Workout level cards row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Use the presets from the ViewModel
                uiState.presets.forEachIndexed { index, preset ->
                    val isFullyVisible = index < 2
                    val isSelected = preset.id == uiState.selectedPresetId

                    WorkoutLevelCard(
                        icon = when (index) {
                            0 -> R.drawable.walking_man_sideview
                            1 -> R.drawable.walking_man_sideview_0
                            else -> R.drawable.walking_man_side_view
                        },
                        title = preset.name,
                        duration = "${preset.totalDurationMinutes} min",
                        backgroundColor = Color(0xFFE8D7C5),
                        isSelected = isSelected,
                        modifier = Modifier.width(if (isFullyVisible) 150.dp else 75.dp),
                        onClick = { viewModel.onPresetSelected(preset.id) }
                    )
                }
            }

            // Start button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        if (uiState.isActivityPermissionGranted) {
                            uiState.selectedPresetId?.let { presetId ->
                                navController.navigate("workout/$presetId")
                            }
                        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            permissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
                        } else {
                            // On older Android, permission is not needed at runtime
                            uiState.selectedPresetId?.let { presetId ->
                                navController.navigate("workout/$presetId")
                            }
                        }
                    },
                    enabled = uiState.selectedPresetId != null,
                    modifier = Modifier
                        .width(200.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00C853),
                        disabledContainerColor = Color(0xFFCCCCCC)
                    ),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.walking_man_sideview),
                        contentDescription = "Start",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Start",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        // Bottom Section
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // View Workout History button
            TextButton(
                onClick = { navController.navigate("history") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "View Workout History",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF0D1C0D)
                )
            }

            // Image section with summary stats overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.minimalist_potted_plant),
                    contentDescription = "Potted plant",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Summary stats overlay
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                        .background(
                            Color.Black.copy(alpha = 0.6f),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    val lastWorkoutText = if (uiState.lastWorkout != null) {
                        val steps = uiState.lastWorkout!!.totalSteps
                        val dateFormat = SimpleDateFormat("MMM d", Locale.getDefault())
                        val date = dateFormat.format(uiState.lastWorkout!!.date)
                        "Last Workout ($date): $steps steps"
                    } else {
                        "No previous workouts"
                    }

                    Text(
                        text = lastWorkoutText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
            }

            // Bottom spacer
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(Color(0xFFF8FAF8))
            )
        }
    }
}

@Composable
fun WorkoutLevelCard(
    icon: Int,
    title: String,
    duration: String,
    backgroundColor: Color,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val cardBackgroundColor = if (isSelected) Color(0xFF00C853) else Color(0xFFE8D7C5)
    val textColor = if (isSelected) Color.White else Color(0xFF0D1C0D)
    val durationColor = if (isSelected) Color(0xFFB8E6B8) else Color(0xFF4CAF50)

    Card(
        modifier = modifier
            .height(180.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = cardBackgroundColor
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Icon area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = title,
                    modifier = Modifier.size(80.dp)
                )
            }

            // Text area
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = textColor
                )
                Text(
                    text = duration,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = durationColor
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    IWTTheme {
        PreviewWithNavController { navController ->
            HomeScreen(
                navController = navController
            )
        }
    }
}
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.binarybricks.iwt.R
import com.binarybricks.iwt.ui.preview.PreviewWithNavController
import com.binarybricks.iwt.ui.screens.home.HomeViewModel
import com.binarybricks.iwt.ui.theme.IWTTheme
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Background color to match toolbar
    val backgroundColor = Color(0xFFF8F8F8)

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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Interval Walk", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { navController.navigate("settings") }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor
                )
            )
        },
        containerColor = backgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(backgroundColor)
        ) {
            // Title
            Text(
                text = "Ready for your walk?",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0D1C0D),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            )

            // Workout level cards - Horizontal carousel
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.presets) { preset ->
                    val isSelected = preset.id == uiState.selectedPresetId
                    WorkoutLevelCard(
                        icon = when (preset.name) {
                            "Beginner" -> R.drawable.walking_man_sideview
                            "Intermediate" -> R.drawable.walking_man_sideview_0
                            else -> R.drawable.walking_man_side_view
                        },
                        title = preset.name,
                        duration = "${preset.totalDurationMinutes} min",
                        isSelected = isSelected,
                        onClick = { viewModel.onPresetSelected(preset.id) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Start button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
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
                        containerColor = Color(0xFF7CEB7C),
                        disabledContainerColor = Color(0xFFCCCCCC)
                    ),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.vector_0_1),
                        contentDescription = "Start",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Start",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // View Workout History button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = { navController.navigate("history") },
                    modifier = Modifier
                        .width(250.dp)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE2F9E2)
                    ),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text(
                        text = "View Workout History",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF0D1C0D)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Image section taking up the rest of the screen height
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
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
                        .clickable(
                            enabled = uiState.lastWorkout != null,
                            onClick = {
                                uiState.lastWorkout?.let { workout ->
                                    navController.navigate("workout_summary/${workout.id}")
                                }
                            }
                        )
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
        }
    }
}

@Composable
fun WorkoutLevelCard(
    icon: Int,
    title: String,
    duration: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val borderColor = if (isSelected) Color(0xFF00C853) else Color.Transparent
    val borderWidth = if (isSelected) 2.dp else 0.dp
    val textColor = Color(0xFF0D1C0D)

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .border(color = borderColor, width = borderWidth, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = title,
            modifier = Modifier.size(200.dp)
        )

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
            color = Color(0xFF4CAF50)
        )
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
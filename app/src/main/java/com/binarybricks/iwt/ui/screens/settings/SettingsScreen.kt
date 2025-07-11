package com.binarybricks.iwt.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.binarybricks.iwt.ui.theme.IWTTheme
import com.binarybricks.iwt.ui.preview.PreviewWithNavController

@Composable
fun SettingsScreenRoute(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    SettingsScreen(
        navController = navController,
        uiState = uiState,
        onSoundCuesToggled = viewModel::onSoundCuesToggled,
        onVibrationCuesToggled = viewModel::onVibrationCuesToggled,
        onKeepScreenOnToggled = viewModel::onKeepScreenOnToggled
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SettingsScreen(
    navController: NavController,
    uiState: SettingsUiState,
    onSoundCuesToggled: (Boolean) -> Unit,
    onVibrationCuesToggled: (Boolean) -> Unit,
    onKeepScreenOnToggled: (Boolean) -> Unit,
    onBackClick: () -> Unit = { navController.popBackStack() }
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
        ) {
            // General Section
            Text(
                text = "General",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F1817),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            )

            // Sound Cues Setting
            SettingItemWithSwitch(
                title = "Sound Cues",
                isChecked = uiState.soundCuesEnabled,
                onCheckedChange = onSoundCuesToggled
            )

            // Vibration Cues Setting
            SettingItemWithSwitch(
                title = "Vibration Cues",
                isChecked = uiState.vibrationCuesEnabled,
                onCheckedChange = onVibrationCuesToggled
            )

            // Keep Screen On Setting
            SettingItemWithSwitch(
                title = "Keep Screen On During Workout",
                isChecked = uiState.keepScreenOnEnabled,
                onCheckedChange = onKeepScreenOnToggled
            )

            Spacer(modifier = Modifier.height(16.dp))

            // About Section
            Text(
                text = "About",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F1817),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            )

            // Privacy Policy Setting
            SettingItem(title = "Privacy Policy")

            // App Version Setting
            SettingItemWithValue(
                title = "App Version",
                value = "1.0.0"
            )

            // Bottom spacer
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            )
        }
    }
}

@Composable
fun SettingItemWithSwitch(
    title: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF8FAF8))
            .padding(horizontal = 16.dp)
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF0F1817),
            modifier = Modifier.weight(1f)
        )

        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF4CAF50),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFFE0E0E0)
            )
        )
    }
}

@Composable
fun SettingItem(
    title: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF8FAF8))
            .padding(horizontal = 16.dp)
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF0F1817)
        )
    }
}

@Composable
fun SettingItemWithValue(
    title: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF8FAF8))
            .padding(horizontal = 16.dp)
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF0F1817)
        )

        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF666666)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    IWTTheme {
        PreviewWithNavController {
            SettingsScreenRoute(navController = it)
        }
    }
}
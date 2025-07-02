package com.binarybricks.iwt.ui.preview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

/**
 * Creates a dummy NavController for previews
 */
@Composable
fun previewNavController(): NavHostController {
    return rememberNavController()
}

/**
 * Wraps content with a NavController for previews
 */
@Composable
fun PreviewWithNavController(
    content: @Composable (NavController) -> Unit
) {
    val navController = previewNavController()
    CompositionLocalProvider {
        content(navController)
    }
}
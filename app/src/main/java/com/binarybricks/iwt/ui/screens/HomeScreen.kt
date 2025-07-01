package com.binarybricks.iwt.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.binarybricks.iwt.R
import com.binarybricks.iwt.ui.theme.IWTTheme

@Composable
fun HomeScreen() {
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
                // Beginner card
                WorkoutLevelCard(
                    icon = R.drawable.walking_man_sideview,
                    title = "Beginner",
                    duration = "20 min",
                    backgroundColor = Color(0xFFE8D7C5),
                    isSelected = false,
                    modifier = Modifier.width(150.dp)
                )

                // Intermediate card
                WorkoutLevelCard(
                    icon = R.drawable.walking_man_sideview_0,
                    title = "Intermediate",
                    duration = "30 min",
                    backgroundColor = Color(0xFFE8D7C5),
                    isSelected = false,
                    modifier = Modifier.width(150.dp)
                )

                // Advanced card (partially visible)
                WorkoutLevelCard(
                    icon = R.drawable.walking_man_side_view,
                    title = "Advanced",
                    duration = "45 min",
                    backgroundColor = Color(0xFFE8D7C5),
                    isSelected = false,
                    modifier = Modifier.width(75.dp)
                )
            }

            // Start button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { /* Handle start action */ },
                    modifier = Modifier
                        .width(200.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00C853)
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
                onClick = { /* Handle statistics action */ },
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
                    Text(
                        text = "Last Workout: 2,500 steps",
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
    modifier: Modifier = Modifier
) {
    val cardBackgroundColor = if (isSelected) Color(0xFF00C853) else Color(0xFFE8D7C5)
    val textColor = if (isSelected) Color.White else Color(0xFF0D1C0D)
    val durationColor = if (isSelected) Color(0xFFB8E6B8) else Color(0xFF4CAF50)

    Card(
        modifier = modifier
            .height(180.dp),
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
        HomeScreen()
    }
}
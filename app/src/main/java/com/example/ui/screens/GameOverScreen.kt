package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Brush

@Composable
fun GameOverScreen(
    onRevive: () -> Unit,
    onMenu: () -> Unit,
    onQuit: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFFFF9800), Color(0xFF64B5F6))
                    )
                )
        )
        Box(modifier = Modifier.fillMaxSize().background(Color.White.copy(alpha = 0.5f)))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "CRASHED!",
                fontSize = 48.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.rotate(-5f)
            )

            Spacer(Modifier.height(32.dp))

            Box(contentAlignment = Alignment.TopCenter) {
                Box(modifier = Modifier.size(200.dp)) {
                    RenderBoy(isJumping = false, isSliding = true)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.offset(y = (-20).dp)) {
                    Icon(Icons.Filled.Star, contentDescription = null, tint = MaterialTheme.colorScheme.inversePrimary, modifier = Modifier.size(32.dp))
                    Icon(Icons.Filled.Star, contentDescription = null, tint = MaterialTheme.colorScheme.inversePrimary, modifier = Modifier.size(40.dp))
                    Icon(Icons.Filled.Star, contentDescription = null, tint = MaterialTheme.colorScheme.inversePrimary, modifier = Modifier.size(32.dp))
                }
            }

            Spacer(Modifier.height(32.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White.copy(alpha = 0.8f), RoundedCornerShape(24.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatRow(title = "SCORE", value = "14,250", icon = Icons.Filled.EmojiEvents, color = MaterialTheme.colorScheme.primaryContainer, iconColor = MaterialTheme.colorScheme.onPrimaryContainer)
                StatRow(title = "COINS", value = "+320", icon = Icons.Filled.MonetizationOn, color = MaterialTheme.colorScheme.secondaryContainer, iconColor = MaterialTheme.colorScheme.onSecondaryContainer)
            }

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = onRevive,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                shape = RoundedCornerShape(24.dp)
            ) {
                Icon(Icons.Filled.Favorite, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Revive! (5)", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                Row(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.onSecondaryContainer, RoundedCornerShape(12.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.MonetizationOn, contentDescription = null, tint = MaterialTheme.colorScheme.inversePrimary, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("500", color = MaterialTheme.colorScheme.onSecondary)
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = onQuit,
                    modifier = Modifier.weight(1f).height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant, contentColor = MaterialTheme.colorScheme.onSurfaceVariant)
                ) {
                    Icon(Icons.Filled.Close, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Quit")
                }
                Button(
                    onClick = onMenu,
                    modifier = Modifier.weight(1f).height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer, contentColor = MaterialTheme.colorScheme.onPrimaryContainer)
                ) {
                    Icon(Icons.Filled.Home, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Menu")
                }
            }
        }
    }
}

@Composable
fun StatRow(title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, iconColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha=0.5f), RoundedCornerShape(16.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.background(color, RoundedCornerShape(12.dp)).padding(8.dp)
            ) {
                Icon(icon, contentDescription = null, tint = iconColor)
            }
            Spacer(Modifier.width(16.dp))
            Text(title, fontWeight = FontWeight.Bold)
        }
        Text(value, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
    }
}

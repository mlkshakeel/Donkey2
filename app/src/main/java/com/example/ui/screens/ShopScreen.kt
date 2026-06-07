package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopScreen(
    onNavigateMenu: () -> Unit,
    onNavigateSettings: () -> Unit,
    onNavigatePlay: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Donkey Run",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                },
                navigationIcon = {
                    Row(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.MonetizationOn, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(4.dp))
                        Text("12,450", fontWeight = FontWeight.Bold)
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.Leaderboard, null, tint = MaterialTheme.colorScheme.primary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f),
                        RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                    )
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomNavItem(icon = Icons.Filled.ShoppingBag, label = "Shop", onClick = {}, isSelected = true)
                BottomNavItem(icon = Icons.Filled.PlayArrow, label = "Play", onClick = onNavigatePlay)
                BottomNavItem(icon = Icons.Filled.Settings, label = "Settings", onClick = onNavigateSettings)
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            // Tabs
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                TabButton(text = "Skins", icon = Icons.Filled.Checkroom, isSelected = selectedTab == 0, onClick = { selectedTab = 0 }, modifier = Modifier.weight(1f))
                TabButton(text = "Upgrades", icon = Icons.Filled.Bolt, isSelected = selectedTab == 1, onClick = { selectedTab = 1 }, modifier = Modifier.weight(1f))
            }

            if (selectedTab == 0) {
                // Skins Content
                HeroSkinCard()
                Spacer(modifier = Modifier.height(16.dp))
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        SkinCard(
                            name = "Cowboy",
                            price = "5,000"
                        )
                    }
                    item {
                        SkinCard(
                            name = "Ninja",
                            price = "7,500",
                            isSale = true
                        )
                    }
                    item {
                        SkinCard(
                            name = "Mecha",
                            price = "15,000",
                            isLocked = true,
                            levelLocked = "Level 10"
                        )
                    }
                }
            } else {
                // Upgrades Content (Simplified)
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    UpgradeItem(title = "Coin Magnet", level = "Lvl 3/5", icon = Icons.Filled.FilterCenterFocus, progress = 0.6f, cost = "2,500")
                    UpgradeItem(title = "Rocket Boost", level = "Lvl 1/5", icon = Icons.Filled.RocketLaunch, progress = 0.2f, cost = "1,000")
                    UpgradeItem(title = "Invincibility", level = "Lvl 5/5", icon = Icons.Filled.Shield, progress = 1f, cost = "Maxed", isMax = true)
                }
            }
        }
    }
}

@Composable
fun TabButton(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier.height(64.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = null)
            Text(text, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun HeroSkinCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(RoundedCornerShape(24.dp))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            RenderBoy(isJumping = false, isSliding = false)
        }
        // Overlay
        Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)))))
        
        Row(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column {
                Text("Astro Donkey", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("+20% Jump Height", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
            }
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer, contentColor = MaterialTheme.colorScheme.onPrimaryContainer)
            ) {
                Text("Equip", fontWeight = FontWeight.Bold)
            }
        }
        
        // Chip
        Row(
            modifier = Modifier
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.tertiaryContainer, RoundedCornerShape(16.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.Stars, contentDescription = null, tint = MaterialTheme.colorScheme.onTertiaryContainer, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(4.dp))
            Text("Legendary", color = MaterialTheme.colorScheme.onTertiaryContainer, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SkinCard(name: String, price: String, isSale: Boolean = false, isLocked: Boolean = false, levelLocked: String = "") {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
            .border(2.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.aspectRatio(1f).clip(RoundedCornerShape(12.dp))) {
            Box(modifier = Modifier.fillMaxSize()) {
                RenderBoy(isJumping = false, isSliding = false)
            }
            if (isSale) {
                Text(
                    "SALE",
                    modifier = Modifier.align(Alignment.TopEnd).padding(4.dp).background(MaterialTheme.colorScheme.error, RoundedCornerShape(8.dp)).padding(horizontal = 4.dp, vertical = 2.dp),
                    color = MaterialTheme.colorScheme.onError,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            if (isLocked) {
                Column(
                    modifier = Modifier.fillMaxSize().background(Color.White.copy(alpha=0.5f)),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Filled.Lock, contentDescription = null, tint = Color.DarkGray)
                    Text(levelLocked, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Text(name, fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.align(Alignment.Start))
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = {},
            enabled = !isLocked,
            modifier = Modifier.fillMaxWidth().height(40.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Icon(Icons.Filled.MonetizationOn, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(4.dp))
            Text(price, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun UpgradeItem(title: String, level: String, icon: androidx.compose.ui.graphics.vector.ImageVector, progress: Float, cost: String, isMax: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(if (isMax) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = if (isMax) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary, modifier = Modifier.size(32.dp))
        }
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(level, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                color = if (isMax) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }
        Spacer(Modifier.width(16.dp))
        Button(
            onClick = {},
            enabled = !isMax,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(if (isMax) "Maxed" else "Upgrade", fontSize = 12.sp)
                if (!isMax) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.MonetizationOn, contentDescription = null, modifier = Modifier.size(12.dp))
                        Text(cost, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

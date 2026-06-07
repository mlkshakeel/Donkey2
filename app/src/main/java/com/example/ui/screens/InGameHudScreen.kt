package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.math.abs
import kotlin.math.sin

data class GameItem3D(
    val id: Int,
    var x: Float, // Lane X (-1.5, 0, 1.5)
    var y: Float, // 0 for ground
    var z: Float, // distance
    val type: Int // 0=Barrier, 1=Coin, 2=Arch
)

data class Renderable(
    val id: String,
    val z: Float,
    val x: Float,
    val y: Float,
    val type: String,
    val extraScale: Float = 1f,
    val rotationZ: Float = 0f
)

@Composable
fun RenderCoin() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val center = Offset(w / 2, h / 2)
        val radius = w * 0.35f
        
        // Outer ring (thick edge)
        drawCircle(Color(0xFFF57F17), radius = radius + w * 0.05f, center = center)
        // Inner part (gold face)
        drawCircle(Color(0xFFFFEB3B), radius = radius, center = center)
        // Embossed Star pattern inside
        val path = Path().apply {
            val numPoints = 5
            val innerRadius = radius * 0.35f
            val outerRadius = radius * 0.7f
            for (i in 0 until numPoints * 2) {
                val angle = i * Math.PI / numPoints - Math.PI / 2
                val r = if (i % 2 == 0) outerRadius else innerRadius
                val x = center.x + (r * Math.cos(angle)).toFloat()
                val y = center.y + (r * Math.sin(angle)).toFloat()
                if (i == 0) moveTo(x, y) else lineTo(x, y)
            }
            close()
        }
        drawPath(path, Color(0xFFF57F17))
        // Specular highlight
        drawCircle(Color.White.copy(alpha=0.4f), radius = radius * 0.3f, center = Offset(center.x - radius*0.3f, center.y - radius*0.3f))
    }
}

@Composable
fun RenderBarrier() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        // Red and white striped hurdle
        drawRect(Color.DarkGray, Offset(w*0.15f, h*0.3f), Size(w*0.1f, h*0.7f)) // Left leg
        drawRect(Color.DarkGray, Offset(w*0.75f, h*0.3f), Size(w*0.1f, h*0.7f)) // Right leg
        
        // Striped bar
        val barTop = h*0.2f
        val barHeight = h*0.3f
        
        clipRect(left = w*0.05f, top = barTop, right = w*0.95f, bottom = barTop + barHeight) {
            drawRect(Color.Red, Offset(w*0.05f, barTop), Size(w*0.9f, barHeight))
            for (i in -1..4) {
                val path = Path().apply {
                    moveTo(w*0.25f * i, barTop + barHeight)
                    lineTo(w*0.25f * i + w*0.15f, barTop)
                    lineTo(w*0.25f * i + w*0.3f, barTop)
                    lineTo(w*0.25f * i + w*0.15f, barTop + barHeight)
                    close()
                }
                drawPath(path, Color.White)
            }
        }
    }
}

@Composable
fun RenderTrain() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        
        val trainColor = Color(0xFFE53935) // Red train body based on screenshot
        val windowColor = Color(0xFF0D47A1)
        val roofColor = Color(0xFF90A4AE)
        val stripeColor = Color(0xFFFFFFFF)
        
        // Main Train Body
        drawRoundRect(trainColor, Offset(w*0.1f, h*0.2f), Size(w*0.8f, h*0.8f), CornerRadius(w*0.1f, w*0.1f))
        
        // Roof
        drawRoundRect(roofColor, Offset(w*0.1f, h*0.15f), Size(w*0.8f, h*0.1f), CornerRadius(w*0.1f, w*0.1f))
        
        // White Stripes on side
        drawRect(stripeColor, Offset(w*0.1f, h*0.35f), Size(w*0.8f, h*0.05f))
        drawRect(stripeColor, Offset(w*0.1f, h*0.65f), Size(w*0.8f, h*0.05f))
        
        // Front Window (Large Glass)
        drawRoundRect(Color.DarkGray, Offset(w*0.2f, h*0.4f), Size(w*0.6f, h*0.23f), CornerRadius(w*0.05f))
        drawRoundRect(windowColor, Offset(w*0.22f, h*0.42f), Size(w*0.56f, h*0.19f), CornerRadius(w*0.03f))
        
        // Headlights
        drawCircle(Color.Yellow, radius = w*0.08f, center = Offset(w*0.3f, h*0.85f))
        drawCircle(Color.Yellow, radius = w*0.08f, center = Offset(w*0.7f, h*0.85f))
        drawCircle(Color.White, radius = w*0.04f, center = Offset(w*0.3f, h*0.85f))
        drawCircle(Color.White, radius = w*0.04f, center = Offset(w*0.7f, h*0.85f))
        
        // Top light
        drawCircle(Color.Red, radius = w*0.05f, center = Offset(w*0.5f, h*0.2f))
        
        // Grill
        drawRect(Color(0xFF212121), Offset(w*0.35f, h*0.75f), Size(w*0.3f, h*0.2f))
        for (i in 0..4) {
            drawRect(Color.Gray, Offset(w*0.38f + (i * w*0.05f), h*0.75f), Size(w*0.02f, h*0.2f))
        }
    }
}

@Composable
fun RenderTree() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        // Trunk
        drawRect(Color(0xFF5D4037), Offset(w*0.4f, h*0.5f), Size(w*0.2f, h*0.5f))
        // Leaves
        drawCircle(Color(0xFF2E7D32), radius = w*0.4f, center = Offset(w*0.5f, h*0.4f))
        drawCircle(Color(0xFF43A047), radius = w*0.35f, center = Offset(w*0.35f, h*0.3f))
        drawCircle(Color(0xFF388E3C), radius = w*0.35f, center = Offset(w*0.65f, h*0.3f))
        // Highlights/Details
        drawCircle(Color(0xFF66BB6A), radius = w*0.15f, center = Offset(w*0.45f, h*0.2f))
    }
}

@Composable
fun RenderBoy(isJumping: Boolean, isSliding: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "legs")
    val legSwing by infiniteTransition.animateFloat(
        initialValue = -25f,
        targetValue = 25f,
        animationSpec = infiniteRepeatable(
            animation = tween(200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "swing"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        
        val shirtColor = Color(0xFFF5F5F5)
        val jeansColor = Color(0xFF1976D2)
        val bagColor = Color(0xFF4CAF50)
        val hatColor = Color(0xFFD32F2F)
        val skinColor = Color(0xFFFFCCBC)
        
        val poseScaleY = if (isSliding) 0.5f else 1f
        val poseOffsetY = if (isSliding) h * 0.4f else if (isJumping) -h*0.2f else 0f
        
        withTransform({
            translate(top = poseOffsetY)
            scale(scaleX = 1f, scaleY = poseScaleY)
        }) {
            val armLegSwing = if (isJumping || isSliding) 0f else legSwing
            
            // Left Leg
            drawRoundRect(jeansColor, Offset(w*0.35f, h*0.6f - (if (armLegSwing>0) armLegSwing else 0f)), Size(w*0.12f, h*0.28f), CornerRadius(w*0.06f))
            // Left Shoe
            drawRoundRect(Color.White, Offset(w*0.32f, h*0.86f - (if (armLegSwing>0) armLegSwing else 0f)), Size(w*0.15f, h*0.06f), CornerRadius(w*0.03f))

            // Right Leg
            drawRoundRect(jeansColor, Offset(w*0.53f, h*0.6f - (if (armLegSwing<0) -armLegSwing else 0f)), Size(w*0.12f, h*0.28f), CornerRadius(w*0.06f))
            // Right Shoe
            drawRoundRect(Color.White, Offset(w*0.53f, h*0.86f - (if (armLegSwing<0) -armLegSwing else 0f)), Size(w*0.15f, h*0.06f), CornerRadius(w*0.03f))

            // Body
            drawRoundRect(shirtColor, Offset(w*0.3f, h*0.35f), Size(w*0.4f, h*0.3f), CornerRadius(w*0.08f))
            
            // Backpack
            drawRoundRect(bagColor, Offset(w*0.35f, h*0.38f), Size(w*0.3f, h*0.2f), CornerRadius(w*0.05f))
            
            // Head
            drawCircle(skinColor, radius = w*0.12f, center = Offset(w*0.5f, h*0.25f))
            
            // Hat (facing backwards)
            drawCircle(hatColor, radius = w*0.13f, center = Offset(w*0.5f, h*0.22f))
            drawRoundRect(hatColor, Offset(w*0.5f, h*0.24f), Size(w*0.2f, h*0.05f), CornerRadius(w*0.02f))
            
            // Arms
            val armSwing = -armLegSwing
            drawRoundRect(skinColor, Offset(w*0.2f, h*0.4f - (if (armSwing>0) armSwing else 0f)), Size(w*0.08f, h*0.2f), CornerRadius(w*0.04f))
            drawRoundRect(skinColor, Offset(w*0.72f, h*0.4f - (if (armSwing<0) -armSwing else 0f)), Size(w*0.08f, h*0.2f), CornerRadius(w*0.04f))
        }
    }
}

@Composable
fun RenderWall() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val wallColor = Color(0xFFC62828) // Red brick
        val shadowColor = Color(0xFF8E0000)
        
        drawRect(wallColor, Offset(0f, 0f), Size(w, h))
        drawCircle(Color.Transparent, radius = w*0.4f, center = Offset(w*0.5f, h*0.8f), blendMode = androidx.compose.ui.graphics.BlendMode.Clear)
        drawCircle(shadowColor, radius = w*0.45f, center = Offset(w*0.5f, h*0.8f), style = androidx.compose.ui.graphics.drawscope.Stroke(width = w*0.1f))
    }
}

@Composable
fun RenderPole() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val poleColor = Color(0xFF37474F)
        
        drawRect(poleColor, Offset(w*0.2f, 0f), Size(w*0.05f, h))
        drawRect(poleColor, Offset(w*0.75f, 0f), Size(w*0.05f, h))
        drawRect(poleColor, Offset(w*0.2f, h*0.1f), Size(w*0.6f, h*0.05f))
        drawRect(poleColor, Offset(w*0.2f, h*0.2f), Size(w*0.6f, h*0.02f))
    }
}

@Composable
fun InGameHudScreen(onCrash: () -> Unit) {
    var isPaused by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var coins by remember { mutableIntStateOf(0) }

    var playerLane by remember { mutableIntStateOf(1) } // 0=Left, 1=Center, 2=Right
    var isJumping by remember { mutableStateOf(false) }
    var isSliding by remember { mutableStateOf(false) }
    
    val targetPlayerX = (playerLane - 1) * 1.5f
    val playerX by animateFloatAsState(
        targetValue = targetPlayerX, 
        animationSpec = tween(150, easing = LinearOutSlowInEasing),
        label = "x"
    )
    
    val playerY by animateFloatAsState(
        targetValue = if (isJumping) 1.5f else if (isSliding) -0.5f else 0f,
        animationSpec = tween(400, easing = FastOutSlowInEasing),
        finishedListener = { 
            if (isJumping) isJumping = false 
            if (isSliding) isSliding = false 
        },
        label = "y"
    )

    var items by remember { mutableStateOf(listOf<GameItem3D>()) }
    var speed by remember { mutableFloatStateOf(0.3f) }
    var nextItemId by remember { mutableIntStateOf(0) }
    var lastEnvSpawnZ by remember { mutableFloatStateOf(40f) }

    var screenWidthPx by remember { mutableFloatStateOf(1080f) }
    var screenHeightPx by remember { mutableFloatStateOf(1920f) }

    // Game Loop
    LaunchedEffect(isPaused) {
        if (isPaused) return@LaunchedEffect
        while (isActive) {
            delay(16) 
            score += 1
            
            val newItems = mutableListOf<GameItem3D>()
            var hitObstacle = false
            
            for (item in items) {
                item.z -= speed
                
                // Collision check near PLAYER_Z (which is 3f)
                if (item.z in 2.5f..3.5f && abs(item.x - playerX) < 0.5f) {
                    if (item.type == 0 && playerY < 0.5f) { // Low barrier hits legs
                        hitObstacle = true
                    } else if (item.type == 2) { // Train hits directly
                        hitObstacle = true
                    } else if (item.type == 1) { // Coin
                        if (abs(playerY - item.y) < 1.0f) {
                            coins += 1
                            score += 50
                            continue
                        }
                    }
                }
                
                if (item.z > 0.5f) {
                    newItems.add(item)
                }
            }
            
            // Spawning game objects
            if (Math.random() < 0.06) {
                val lane = (0..2).random()
                val type = when (Math.random()) {
                    in 0.0..0.45 -> 1 // 45% coin
                    in 0.45..0.75 -> 0 // 30% barrier
                    else -> 2 // 25% train
                }
                val laneX = (lane - 1) * 1.5f
                if (newItems.none { it.z > 35f && it.x == laneX }) {
                    newItems.add(GameItem3D(nextItemId++, laneX, 0f, 40f, type))
                }
            }
            
            // Spawn continuous environment
            lastEnvSpawnZ -= speed
            if (lastEnvSpawnZ < 30f) {
                // Time to spawn new scenery at z=40f
                if (Math.random() < 0.4) {
                     newItems.add(GameItem3D(nextItemId++, 0f, 0f, 40f, 5)) // Overhead Pole
                } else if (Math.random() < 0.3) {
                     newItems.add(GameItem3D(nextItemId++, -4f, -0.5f, 40f, 4)) // Red Arch Wall Left
                     newItems.add(GameItem3D(nextItemId++, 4f, -0.5f, 40f, 4)) // Red Arch Wall Right
                } else {
                     newItems.add(GameItem3D(nextItemId++, -3.5f, 0f, 40f, 3)) // Tree
                     newItems.add(GameItem3D(nextItemId++, 3.5f, 0f, 40f, 3)) // Tree
                }
                lastEnvSpawnZ += 15f
            }
            // Coin chains
            if (Math.random() < 0.2f && newItems.isNotEmpty()) {
                val lastItem = newItems.last()
                if (lastItem.type == 1 && lastItem.z > 38f) {
                    newItems.add(GameItem3D(nextItemId++, lastItem.x, 0f, lastItem.z + 1.5f, 1))
                }
            }
            
            if (hitObstacle) {
                onCrash()
                return@LaunchedEffect
            }
            
            items = newItems
            speed += 0.00005f // gradually increase speed
        }
    }

    var startOffset by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(isPaused) {
                if (isPaused) return@pointerInput
                detectDragGestures(
                    onDragStart = { startOffset = it },
                    onDragEnd = { startOffset = Offset.Zero }
                ) { change, _ ->
                    val current = change.position
                    val dx = current.x - startOffset.x
                    val dy = current.y - startOffset.y
                    
                    if (abs(dx) > 50f || abs(dy) > 50f) {
                        if (abs(dx) > abs(dy)) {
                            // Left/Right
                            if (dx > 0 && playerLane < 2) playerLane++
                            else if (dx < 0 && playerLane > 0) playerLane--
                        } else {
                            // Up/Down
                            if (dy < 0 && !isJumping && !isSliding) isJumping = true
                            else if (dy > 0 && !isSliding && !isJumping) isSliding = true
                        }
                        // Reset to prevent multiple triggers in one long continuous swipe
                        startOffset = current 
                    }
                }
            }
            .onSizeChanged {
                screenWidthPx = it.width.toFloat()
                screenHeightPx = it.height.toFloat()
            }
    ) {
        
        // Render 3D Background & Floor
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val fov = w * 0.85f
            val horizonY = h * 0.4f
            val cameraY = 1.0f
            
            // Sky gradient (Vibrant Subway Sky)
            drawRect(
                brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                    listOf(Color(0xFF9C27B0), Color(0xFFFF9800), Color(0xFFFFCC80)) // Purple to Orange sunset
                ),
                topLeft = Offset.Zero,
                size = Size(w, horizonY)
            )
            
            // Skyline (Buildings) side scroll effect based on score
            val bgScroll = (score * 0.2f) % (w * 0.5f)
            for (i in -1..2) {
                val bx = i * w*0.5f - bgScroll
                drawRect(Color(0xFF8D6E63), Offset(bx + w*0.05f, horizonY - h*0.2f), Size(w*0.15f, h*0.2f))
                drawRect(Color(0xFF795548), Offset(bx + w*0.2f, horizonY - h*0.15f), Size(w*0.1f, h*0.15f))
                drawRect(Color(0xFFA1887F), Offset(bx + w*0.35f, horizonY - h*0.25f), Size(w*0.1f, h*0.25f))
            }

            // Green grass border
            drawRect(Color(0xFF8BC34A), Offset(0f, horizonY), Size(w, h - horizonY))
            
            // Ground Track Base (Gravel)
            val gravelPath = Path().apply {
                 moveTo(0f, h)
                 lineTo(w/2 - (2.5f * fov) / 50f, horizonY)
                 lineTo(w/2 + (2.5f * fov) / 50f, horizonY)
                 lineTo(w, h)
                 close()
            }
            drawPath(gravelPath, Color(0xFF6D4C41)) // Dark brown
            
            // Moving floor tracks (Wooden Sleepers)
            val zTravel = score * speed * 0.1f 
            val sleeperSpacing = 2.5f
            val offset = zTravel % sleeperSpacing
            
            for (i in 0..25) {
                val z = i * sleeperSpacing - offset + 1f
                if (z > 0.5f && z < 60f) {
                    val y = horizonY + (cameraY * fov) / z
                    if (y in horizonY..h) {
                        val leftX = w/2 + (-2.2f * fov) / z
                        val rightX = w/2 + (2.2f * fov) / z
                        val thickness = (20f / z).coerceAtLeast(3f)
                        drawLine(Color(0xFF4E342E), Offset(leftX, y), Offset(rightX, y), strokeWidth = thickness)
                    }
                }
            }
            
            // Draw 3 Railway Rails across the gravel
            val lanesX = listOf(-1.5f, 0f, 1.5f)
            for (lx in lanesX) {
                val rails = listOf(lx - 0.35f, lx + 0.35f)
                for (rx in rails) {
                    val fx = w/2 + (rx * fov) / 60f
                    val fy = horizonY + (cameraY * fov) / 60f
                    val nx = w/2 + (rx * fov) / 0.5f
                    val ny = horizonY + (cameraY * fov) / 0.5f
                    
                    val railPath = Path().apply {
                         moveTo(fx - 1f, fy)
                         lineTo(fx + 1f, fy)
                         lineTo(nx + 6f, ny)
                         lineTo(nx - 6f, ny)
                         close()
                    }
                    drawPath(railPath, Color(0xFFB0BEC5)) // Steel grey
                }
            }
        }

        // Render sorted 3D Objects
        Box(modifier = Modifier.fillMaxSize()) {
            val fov = screenWidthPx * 0.85f
            val horizonY = screenHeightPx * 0.4f
            val cameraY = 1.0f

            val renderables = mutableListOf<Renderable>()
            val wobble = if (!isJumping && !isSliding && !isPaused) sin(score * 0.4f) * 8f else 0f
            
            renderables.add(Renderable("player", 3f, playerX, playerY, "player", if (isSliding) 0.5f else 1f, wobble))
            items.forEach { 
                var extraScale = 1f
                if (it.type == 2) extraScale = 2.5f
                if (it.type == 3) extraScale = 2.0f
                if (it.type == 4) extraScale = 4.0f // Walls are huge
                if (it.type == 5) extraScale = 3.0f // Poles are huge
                renderables.add(Renderable("item_${it.id}", it.z, it.x, it.y, it.type.toString(), extraScale))
            }
            
            renderables.sortByDescending { it.z }

            renderables.forEach { r ->
                val scale = 3f / r.z
                val screenX = screenWidthPx / 2f + (r.x * fov) / r.z
                val screenY = horizonY + ((cameraY - r.y) * fov) / r.z
                
                if (r.z > 0.5f) {
                    val boxSize = 120.dp
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .graphicsLayer {
                                val pxSize = boxSize.toPx()
                                translationX = screenX - (pxSize / 2f)
                                translationY = screenY - pxSize
                                transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0.5f, 1f)
                                scaleX = scale * if (r.type == "player") 1f else r.extraScale
                                scaleY = scale * r.extraScale
                                if (r.rotationZ != 0f) {
                                    rotationZ = r.rotationZ
                                }
                                alpha = if (r.z > 40f) (50f - r.z) / 10f else 1f
                            }
                            // GraphicsLayer allows draw size modifier
                            .size(if (r.type == "5") boxSize * 2f else boxSize) // larger box for pole to span across
                    ) {
                        when (r.type) {
                            "player" -> {
                                RenderBoy(isJumping = isJumping, isSliding = isSliding)
                            }
                            "0" -> RenderBarrier() // Jump over
                            "1" -> RenderCoin() // Collect
                            "2" -> RenderTrain() // Dodge
                            "3" -> RenderTree() // Decor
                            "4" -> RenderWall() // Decor Side Wall
                            "5" -> RenderPole() // Decor Arch Frame
                        }
                    }
                }
            }
        }
        
        // Dim overlay if paused
        if (isPaused) {
            Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)))
        }

        // Top HUD
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            // Coins
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(Color.White.copy(alpha = 0.8f), CircleShape)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Box(
                    modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer, CircleShape).padding(4.dp)
                ) {
                    Icon(Icons.Filled.MonetizationOn, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer)
                }
                Spacer(Modifier.width(8.dp))
                Text(String.format("%06d", coins), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }

            // Pause Button
            IconButton(
                onClick = { isPaused = !isPaused },
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.inverseSurface.copy(alpha=0.8f), CircleShape)
                    .size(48.dp)
            ) {
                Icon(if (isPaused) Icons.Filled.PlayArrow else Icons.Filled.Pause, contentDescription = "Pause", tint = MaterialTheme.colorScheme.inverseOnSurface)
            }

            // Score
            Column(horizontalAlignment = Alignment.End) {
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier
                        .background(Color.White.copy(alpha=0.8f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text("SCORE", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(String.format("%,d", score), fontWeight = FontWeight.ExtraBold, fontSize = 28.sp, color = MaterialTheme.colorScheme.primary)
                }
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(16.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Filled.Close, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onPrimaryContainer)
                    Text("x10", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }
        }

        // Bottom Power-up Bars
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PowerUpBar(icon = "💧", color = MaterialTheme.colorScheme.secondary, progress = 0.5f)
            PowerUpBar(icon = "🚀", color = MaterialTheme.colorScheme.primary, progress = 0.3f)
        }

        // Instructions
        if (!isPaused && score < 200) {
            Text(
                "Swipe Left/Right to turn.\nSwipe Up to Jump.\nSwipe Down to Slide.",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = (-50).dp)
                    .background(Color.Black.copy(alpha=0.6f), RoundedCornerShape(12.dp))
                    .padding(16.dp)
            )
        }
        
        // Debug/Fallback crash button in case they get stuck behind an impossible wall
        if (isPaused) {
            Button(onClick = onCrash, modifier = Modifier.align(Alignment.Center).offset(y = 50.dp)) {
                Text("End Run Manually")
            }
        }
    }
}

@Composable
fun PowerUpBar(icon: String, color: Color, progress: Float) {
    Row(
        modifier = Modifier
            .background(Color.White.copy(alpha=0.8f), RoundedCornerShape(12.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.background(color.copy(alpha=0.3f), CircleShape).padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(icon, fontSize = 16.sp)
        }
        Spacer(Modifier.width(16.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.width(100.dp).height(12.dp).clip(RoundedCornerShape(6.dp)),
            color = color,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

package com.cocakil2545.saudiindiaclock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

private val Navy = Color(0xFF07111F)
private val Panel = Color(0xFF111F31)
private val SaudiGreen = Color(0xFF35D39A)
private val IndiaOrange = Color(0xFFFFA45B)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { ClockApp() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClockApp() {
    var now by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var use24Hour by remember { mutableStateOf(false) }
    var darkMode by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        while (true) {
            now = System.currentTimeMillis()
            delay(1000)
        }
    }

    val saudi = ZonedDateTime.ofInstant(
        Instant.ofEpochMilli(now), ZoneId.of("Asia/Riyadh")
    )
    val india = ZonedDateTime.ofInstant(
        Instant.ofEpochMilli(now), ZoneId.of("Asia/Kolkata")
    )

    val fg = if (darkMode) Color(0xFFF4F8FF) else Color(0xFF142238)
    val muted = if (darkMode) Color(0xFF91A4BB) else Color(0xFF64758A)
    val panel = if (darkMode) Panel else Color.White

    MaterialTheme(
        colorScheme = if (darkMode) darkColorScheme() else lightColorScheme()
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    if (darkMode) Brush.verticalGradient(
                        listOf(Color(0xFF0B1A2B), Navy, Color(0xFF050B13))
                    ) else Brush.verticalGradient(
                        listOf(Color(0xFFEAF2FA), Color(0xFFF8FAFD))
                    )
                )
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 22.dp, vertical = 25.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(
                            "GLOBAL TIME",
                            color = muted,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.5.sp
                        )
                        Spacer(Modifier.height(5.dp))
                        Text(
                            "Two countries.\nOne connection.",
                            color = fg,
                            fontSize = 25.sp,
                            lineHeight = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    IconButton(
                        onClick = { darkMode = !darkMode },
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(panel)
                            .border(
                                1.dp,
                                muted.copy(alpha = 0.22f),
                                CircleShape
                            )
                    ) {
                        Icon(
                            if (darkMode) Icons.Default.LightMode
                            else Icons.Default.DarkMode,
                            contentDescription = "Toggle theme",
                            tint = fg
                        )
                    }
                }

                Spacer(Modifier.height(23.dp))

                ClockPanel(
                    "SAUDI ARABIA", "Riyadh", "🇸🇦",
                    "AST · UTC+3", saudi, use24Hour,
                    fg, muted, SaudiGreen, panel, darkMode
                )

                Spacer(Modifier.height(15.dp))

                ClockPanel(
                    "INDIA", "India Standard Time", "🇮🇳",
                    "IST · UTC+5:30", india, use24Hour,
                    fg, muted, IndiaOrange, panel, darkMode
                )

                Spacer(Modifier.height(17.dp))

                Row(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(panel)
                        .border(
                            1.dp,
                            muted.copy(alpha = 0.18f),
                            RoundedCornerShape(20.dp)
                        )
                        .padding(17.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(
                            "TIME DIFFERENCE",
                            color = muted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.6.sp
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "India is ahead",
                            color = fg,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Text(
                        "+2h 30m",
                        color = SaudiGreen,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(19.dp))

                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(
                            "DISPLAY",
                            color = muted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.8.sp
                        )
                        Text(
                            "Time format",
                            color = fg,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    SingleChoiceSegmentedButtonRow {
                        SegmentedButton(
                            selected = !use24Hour,
                            onClick = { use24Hour = false },
                            shape = SegmentedButtonDefaults.itemShape(0, 2)
                        ) { Text("12H") }

                        SegmentedButton(
                            selected = use24Hour,
                            onClick = { use24Hour = true },
                            shape = SegmentedButtonDefaults.itemShape(1, 2)
                        ) { Text("24H") }
                    }
                }

                Spacer(Modifier.height(23.dp))

                Text(
                    "LIVE SYNC  •  OFFLINE READY",
                    color = muted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.8.sp
                )

                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun ClockPanel(
    country: String,
    city: String,
    flag: String,
    zone: String,
    time: ZonedDateTime,
    use24Hour: Boolean,
    foreground: Color,
    muted: Color,
    accent: Color,
    panel: Color,
    darkMode: Boolean
) {
    val pattern = if (use24Hour) "HH:mm:ss" else "hh:mm:ss a"

    val timeText = time.format(
        DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH)
    )

    val dateText = time.format(
        DateTimeFormatter.ofPattern("EEEE, dd MMM yyyy", Locale.ENGLISH)
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(containerColor = panel),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (darkMode) 0.dp else 3.dp
        )
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(19.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(flag, fontSize = 28.sp)
                Spacer(Modifier.width(12.dp))

                Column(Modifier.weight(1f)) {
                    Text(
                        country,
                        color = muted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.6.sp
                    )
                    Text(
                        city,
                        color = foreground,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Box(
                    Modifier
                        .clip(RoundedCornerShape(30.dp))
                        .background(accent.copy(alpha = 0.13f))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        zone,
                        color = accent,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            Text(
                timeText,
                modifier = Modifier.fillMaxWidth(),
                color = foreground,
                fontSize = if (use24Hour) 43.sp else 39.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center,
                letterSpacing = (-1.5).sp,
                maxLines = 1
            )

            Spacer(Modifier.height(7.dp))

            Text(
                dateText,
                modifier = Modifier.fillMaxWidth(),
                color = muted,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(15.dp))

            Box(
                Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .clip(CircleShape)
                    .background(accent.copy(alpha = 0.75f))
            )
        }
    }
}

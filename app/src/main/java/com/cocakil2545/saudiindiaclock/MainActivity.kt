package com.cocakil2545.saudiindiaclock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    var darkMode by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (true) {
            now = System.currentTimeMillis()
            delay(1000)
        }
    }

    val background = if (darkMode) Color(0xFF101820) else Color(0xFFF3F6FA)
    val foreground = if (darkMode) Color(0xFFF5F7FA) else Color(0xFF17212B)
    val muted = if (darkMode) Color(0xFFB0BEC9) else Color(0xFF637381)

    val saudi = ZonedDateTime.ofInstant(
        Instant.ofEpochMilli(now), ZoneId.of("Asia/Riyadh")
    )
    val india = ZonedDateTime.ofInstant(
        Instant.ofEpochMilli(now), ZoneId.of("Asia/Kolkata")
    )

    MaterialTheme(
        colorScheme = if (darkMode) darkColorScheme() else lightColorScheme()
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 22.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "WORLD CLOCK",
                            color = muted,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                        Text(
                            "Saudi Arabia · India",
                            color = foreground,
                            fontSize = 21.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    IconButton(onClick = { darkMode = !darkMode }) {
                        Icon(
                            if (darkMode) Icons.Default.LightMode
                            else Icons.Default.DarkMode,
                            contentDescription = "Toggle theme",
                            tint = foreground
                        )
                    }
                }

                ClockCard(
                    "SAUDI ARABIA", "Riyadh", "🇸🇦", "AST · UTC+3",
                    saudi, use24Hour, foreground, muted, Color(0xFF20A36B), darkMode
                )

                ClockCard(
                    "INDIA", "India Standard Time", "🇮🇳", "IST · UTC+5:30",
                    india, use24Hour, foreground, muted, Color(0xFFEF8B35), darkMode
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "TIME FORMAT",
                            color = muted,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp
                        )
                        Text(
                            if (use24Hour) "24-hour clock" else "12-hour clock",
                            color = foreground,
                            fontSize = 14.sp
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

                Spacer(Modifier.weight(1f))

                Text(
                    "Always in sync · Works offline",
                    color = muted,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun ClockCard(
    country: String,
    city: String,
    flag: String,
    zone: String,
    time: ZonedDateTime,
    use24Hour: Boolean,
    foreground: Color,
    muted: Color,
    accent: Color,
    darkMode: Boolean
) {
    val pattern = if (use24Hour) "HH:mm:ss" else "hh:mm:ss a"
    val timeText = time.format(
        DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH)
    )
    val dateText = time.format(
        DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", Locale.ENGLISH)
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (darkMode) Color(0xFF1D2A35) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(flag, fontSize = 27.sp)
                Spacer(Modifier.width(12.dp))

                Column(Modifier.weight(1f)) {
                    Text(
                        country,
                        color = muted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.6.sp
                    )
                    Text(
                        city,
                        color = foreground,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Box(
                    Modifier
                        .background(
                            accent.copy(alpha = 0.13f),
                            RoundedCornerShape(50)
                        )
                        .padding(horizontal = 11.dp, vertical = 6.dp)
                ) {
                    Text(
                        zone,
                        color = accent,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(23.dp))

            Text(
                timeText,
                modifier = Modifier.fillMaxWidth(),
                color = foreground,
                fontSize = 42.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center,
                letterSpacing = (-1).sp,
                maxLines = 1
            )

            Spacer(Modifier.height(7.dp))

            Text(
                dateText,
                modifier = Modifier.fillMaxWidth(),
                color = muted,
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

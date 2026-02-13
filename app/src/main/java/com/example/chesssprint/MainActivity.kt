package com.example.chesssprint

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ChessSprintScreen()
                }
            }
        }
    }
}

@Composable
fun ChessSprintScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(Color(0xFF101828), Color(0xFF1E293B))
                )
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            HeaderCard()
        }

        item {
            RulesCard()
        }

        items(sevenDayPlan) { day ->
            DayCard(day)
        }
    }
}

@Composable
private fun HeaderCard() {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xCC0F172A))
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("7-Day Chess Sprint", style = MaterialTheme.typography.headlineSmall, color = Color.White)
            Text("Built for your 12:00 AM wake-up routine • 4 hours/day", color = Color(0xFFD1D5DB))
            Text(
                "Goal: Improve fast while avoiding tilt. Focus on quality Rapid games and structured review.",
                color = Color(0xFF94A3B8)
            )
        }
    }
}

@Composable
private fun RulesCard() {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xCC172554))
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Non-negotiable rules", color = Color.White, fontWeight = FontWeight.Bold)
            RuleItem("Only Rapid (10|5 or 15|10). No blitz or bullet.")
            RuleItem("Stop after 2 losses in a row.")
            RuleItem("Use one simple opening set for all 7 days.")
            RuleItem("Review every game, even wins.")
        }
    }
}

@Composable
private fun RuleItem(text: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.Top) {
        Text("•", color = Color(0xFFF8FAFC))
        Text(text, color = Color(0xFFE2E8F0))
    }
}

@Composable
private fun DayCard(dayPlan: DayPlan) {
    var done by rememberSaveable(dayPlan.day) { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xCC0B1120))
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(dayPlan.day, color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                AssistChip(
                    onClick = { },
                    label = { Text(if (done) "Completed" else "In Progress") },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = if (done) Color(0xFF166534) else Color(0xFF1D4ED8),
                        labelColor = Color.White
                    )
                )
                Checkbox(checked = done, onCheckedChange = { done = it })
            }

            Text(dayPlan.focus, color = Color(0xFF93C5FD), fontWeight = FontWeight.SemiBold)

            dayPlan.blocks.forEach { block ->
                BlockRow(block)
            }
        }
    }
}

@Composable
private fun BlockRow(block: BlockPlan) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(block.title, color = Color(0xFFE2E8F0), fontWeight = FontWeight.Medium)
        Text(block.details, color = Color(0xFF94A3B8), style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(4.dp))
    }
}

data class DayPlan(
    val day: String,
    val focus: String,
    val blocks: List<BlockPlan>
)

data class BlockPlan(
    val title: String,
    val details: String
)

private val dailyStructure = listOf(
    BlockPlan("Block A (45 min)", "Warm-up puzzles, then 10 serious puzzles. Write 2 tactical reminders."),
    BlockPlan("Block B (45 min)", "25 min endgame topic + 20 min opening ideas."),
    BlockPlan("Block C (90 min)", "2 Rapid games with quick review after each game."),
    BlockPlan("Block D (60 min)", "Deep review worst game + remedy puzzles + 3-line journal.")
)

private val sevenDayPlan = listOf(
    DayPlan("Day 1", "Baseline: stop hanging material. Endgames: K+R vs K and K+Q vs K.", dailyStructure),
    DayPlan("Day 2", "Forks & double attacks. Endgame: activate your king.", dailyStructure),
    DayPlan("Day 3", "Pins, skewers, discovered attacks. Endgame: opposition basics.", dailyStructure),
    DayPlan("Day 4", "Mate patterns: back-rank, ladder, mate in 2-3.", dailyStructure),
    DayPlan("Day 5", "Calculation discipline: forcing moves, 2-3 ply deep.", dailyStructure),
    DayPlan("Day 6", "Conversion day: turn winning positions into wins.", dailyStructure),
    DayPlan("Day 7", "Test day: up to 3 Rapid games with stop-loss still active.", dailyStructure)
)

@Preview(showBackground = true)
@Composable
private fun PreviewChessSprintScreen() {
    MaterialTheme {
        ChessSprintScreen()
    }
}

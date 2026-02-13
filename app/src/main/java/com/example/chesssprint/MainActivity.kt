package com.example.chesssprint

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ChessSprintApp()
                }
            }
        }
    }
}

@Composable
private fun ChessSprintApp() {
    var tab by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(selected = tab == 0, onClick = { tab = 0 }, label = { Text("Play") }, icon = { Text("♟") })
                NavigationBarItem(selected = tab == 1, onClick = { tab = 1 }, label = { Text("Routine") }, icon = { Text("📅") })
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (tab == 0) PlayScreen() else RoutineScreen()
        }
    }
}

@Composable
private fun PlayScreen() {
    var state by rememberSaveable { mutableStateOf(ChessState.initial()) }
    var selected by rememberSaveable { mutableStateOf<Int?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF0F172A), Color(0xFF111827))))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("Chess Practice Board", color = Color.White, style = MaterialTheme.typography.headlineSmall)
        Text("Turn: ${if (state.whiteToMove) "White" else "Black"}", color = Color(0xFFBFDBFE))

        ChessBoard(
            state = state,
            selected = selected,
            onSquareTap = { index ->
                val piece = state.board[index]
                val current = selected
                if (current == null) {
                    if (piece != null && piece.isWhite == state.whiteToMove) selected = index
                } else {
                    if (current == index) {
                        selected = null
                    } else {
                        val moved = state.tryMove(current, index)
                        if (moved != null) {
                            state = moved
                            selected = null
                        } else if (piece != null && piece.isWhite == state.whiteToMove) {
                            selected = index
                        }
                    }
                }
            }
        )

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(onClick = {
                state = ChessState.initial()
                selected = null
            }) { Text("Reset") }
            Button(onClick = {
                state = state.withRandomBotMove()
                selected = null
            }) { Text("Bot move") }
        }
        Text("This is a training board with core piece movement and captures.", color = Color(0xFF94A3B8))
    }
}

@Composable
private fun ChessBoard(state: ChessState, selected: Int?, onSquareTap: (Int) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        for (row in 0..7) {
            Row(modifier = Modifier.fillMaxWidth()) {
                for (col in 0..7) {
                    val idx = row * 8 + col
                    val isLight = (row + col) % 2 == 0
                    val bg = if (isLight) Color(0xFFFDE68A) else Color(0xFF92400E)
                    val isSelected = selected == idx
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .background(bg)
                            .border(if (isSelected) 3.dp else 0.dp, Color(0xFF0EA5E9))
                            .clickable { onSquareTap(idx) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(state.board[idx]?.symbol ?: "", color = Color.White, textAlign = TextAlign.Center)
                    }
                }
            }
        }
    }
}

@Composable
private fun RoutineScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF101828), Color(0xFF1E293B))))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { HeaderCard() }
        item { RulesCard() }
        items(sevenDayPlan) { day -> DayCard(day) }
    }
}

@Composable
private fun HeaderCard() {
    Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color(0xCC0F172A))) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("7-Day Chess Sprint", style = MaterialTheme.typography.headlineSmall, color = Color.White)
            Text("Built for your 12:00 AM wake-up routine • 4 hours/day", color = Color(0xFFD1D5DB))
        }
    }
}

@Composable
private fun RulesCard() {
    Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color(0xCC172554))) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Non-negotiable rules", color = Color.White, fontWeight = FontWeight.Bold)
            RuleItem("Only Rapid (10|5 or 15|10).")
            RuleItem("Stop after 2 losses in a row.")
            RuleItem("Review every game.")
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
    Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color(0xCC0B1120))) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(dayPlan.day, color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                Checkbox(checked = done, onCheckedChange = { done = it })
            }
            Text(dayPlan.focus, color = Color(0xFF93C5FD), fontWeight = FontWeight.SemiBold)
            dayPlan.blocks.forEach {
                Text(it.title, color = Color(0xFFE2E8F0), fontWeight = FontWeight.Medium)
                Text(it.details, color = Color(0xFF94A3B8), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

data class DayPlan(val day: String, val focus: String, val blocks: List<BlockPlan>)
data class BlockPlan(val title: String, val details: String)

enum class PieceType { KING, QUEEN, ROOK, BISHOP, KNIGHT, PAWN }
data class Piece(val type: PieceType, val isWhite: Boolean) {
    val symbol: String
        get() = when (type) {
            PieceType.KING -> if (isWhite) "♔" else "♚"
            PieceType.QUEEN -> if (isWhite) "♕" else "♛"
            PieceType.ROOK -> if (isWhite) "♖" else "♜"
            PieceType.BISHOP -> if (isWhite) "♗" else "♝"
            PieceType.KNIGHT -> if (isWhite) "♘" else "♞"
            PieceType.PAWN -> if (isWhite) "♙" else "♟"
        }
}

data class ChessState(val board: List<Piece?>, val whiteToMove: Boolean) {
    fun tryMove(from: Int, to: Int): ChessState? {
        val piece = board[from] ?: return null
        if (piece.isWhite != whiteToMove || from == to) return null
        val target = board[to]
        if (target?.isWhite == piece.isWhite) return null
        if (!isLegalPieceMove(piece, from, to)) return null

        val mutable = board.toMutableList()
        mutable[to] = piece
        mutable[from] = null
        return ChessState(mutable, !whiteToMove)
    }

    fun withRandomBotMove(): ChessState {
        val moves = mutableListOf<Pair<Int, Int>>()
        board.indices.forEach { from ->
            val piece = board[from] ?: return@forEach
            if (piece.isWhite != whiteToMove) return@forEach
            board.indices.forEach { to ->
                if (tryMove(from, to) != null) moves.add(from to to)
            }
        }
        if (moves.isEmpty()) return this
        val move = moves.random()
        return tryMove(move.first, move.second) ?: this
    }

    private fun isLegalPieceMove(piece: Piece, from: Int, to: Int): Boolean {
        val fromRow = from / 8
        val fromCol = from % 8
        val toRow = to / 8
        val toCol = to % 8
        val dr = toRow - fromRow
        val dc = toCol - fromCol
        val dir = if (piece.isWhite) -1 else 1

        return when (piece.type) {
            PieceType.KING -> kotlin.math.abs(dr) <= 1 && kotlin.math.abs(dc) <= 1
            PieceType.QUEEN -> isClearLine(fromRow, fromCol, toRow, toCol)
            PieceType.ROOK -> (dr == 0 || dc == 0) && isClearLine(fromRow, fromCol, toRow, toCol)
            PieceType.BISHOP -> kotlin.math.abs(dr) == kotlin.math.abs(dc) && isClearLine(fromRow, fromCol, toRow, toCol)
            PieceType.KNIGHT -> (kotlin.math.abs(dr) == 2 && kotlin.math.abs(dc) == 1) || (kotlin.math.abs(dr) == 1 && kotlin.math.abs(dc) == 2)
            PieceType.PAWN -> {
                val target = board[to]
                val startRow = if (piece.isWhite) 6 else 1
                when {
                    dc == 0 && target == null && dr == dir -> true
                    dc == 0 && target == null && fromRow == startRow && dr == dir * 2 -> {
                        board[(fromRow + dir) * 8 + fromCol] == null
                    }
                    kotlin.math.abs(dc) == 1 && dr == dir && target != null && target.isWhite != piece.isWhite -> true
                    else -> false
                }
            }
        }
    }

    private fun isClearLine(fromRow: Int, fromCol: Int, toRow: Int, toCol: Int): Boolean {
        val stepR = (toRow - fromRow).coerceIn(-1, 1)
        val stepC = (toCol - fromCol).coerceIn(-1, 1)
        if (stepR == 0 && stepC == 0) return false
        var r = fromRow + stepR
        var c = fromCol + stepC
        while (r != toRow || c != toCol) {
            if (board[r * 8 + c] != null) return false
            r += stepR
            c += stepC
        }
        return true
    }

    companion object {
        fun initial(): ChessState {
            val b = MutableList<Piece?>(64) { null }
            fun place(row: Int, col: Int, type: PieceType, white: Boolean) { b[row * 8 + col] = Piece(type, white) }
            val back = listOf(PieceType.ROOK, PieceType.KNIGHT, PieceType.BISHOP, PieceType.QUEEN, PieceType.KING, PieceType.BISHOP, PieceType.KNIGHT, PieceType.ROOK)
            back.forEachIndexed { c, t -> place(0, c, t, false); place(7, c, t, true) }
            (0..7).forEach { c -> place(1, c, PieceType.PAWN, false); place(6, c, PieceType.PAWN, true) }
            return ChessState(b, true)
        }
    }
}

private val dailyStructure = listOf(
    BlockPlan("Block A (45 min)", "Warm-up puzzles + 10 serious puzzles."),
    BlockPlan("Block B (45 min)", "25 min endgame topic + 20 min opening ideas."),
    BlockPlan("Block C (90 min)", "2 Rapid games with quick review."),
    BlockPlan("Block D (60 min)", "Deep review + remedy puzzles + journal.")
)

private val sevenDayPlan = listOf(
    DayPlan("Day 1", "Baseline: stop hanging pieces.", dailyStructure),
    DayPlan("Day 2", "Forks & double attacks.", dailyStructure),
    DayPlan("Day 3", "Pins, skewers, discovered attacks.", dailyStructure),
    DayPlan("Day 4", "Mate patterns.", dailyStructure),
    DayPlan("Day 5", "Calculation discipline.", dailyStructure),
    DayPlan("Day 6", "Conversion day.", dailyStructure),
    DayPlan("Day 7", "Test day (controlled rating push).", dailyStructure)
)

package com.example.expensetrack

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
@Composable
@Preview(showBackground = true)
fun Home() {
    Surface(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (header, card, body) = createRefs()

            // 🟩 Header Box
            Box(
                modifier = Modifier
                    .constrainAs(header) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFA8D5BA),
                                Color(0xFFBCEBCB).copy(alpha = 0.6f)
                            )
                        )
                    )
                    .padding(start = 16.dp, top = 32.dp)
            ) {
                Column {
                    Text(
                        text = "Expenso",
                        fontSize = 24.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "Track Your Expenses",
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                }

                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = Color.Black,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 16.dp)
                )
            }

            // 🟦 Overlapping Card (Expense Summary)
            ExpenseSummaryCard(
                modifier = Modifier.constrainAs(card) {
                    top.linkTo(header.bottom, margin = (-40).dp)
                    centerHorizontallyTo(parent)
                }
            )

            // ⬜ Body Placeholder
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .constrainAs(body) {
                        top.linkTo(card.bottom, margin = 24.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                // Add body content here
            }
        }
    }
}

@Composable
fun ExpenseSummaryCard(
    balance: String = "₹12,345",
    totalSpent: String = "₹7,890",
    transactions: Int = 42,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth(0.9f)
            .height(180.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4A148C))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Text("Current Balance", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
            Text(balance, color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Total Spent", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                    Text(totalSpent, color = Color.White, fontSize = 16.sp)
                }

                Column {
                    Text("Transactions", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                    Text(transactions.toString(), color = Color.White, fontSize = 16.sp)
                }
            }
        }
    }
}

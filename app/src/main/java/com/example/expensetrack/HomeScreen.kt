package com.example.expensetrack

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

@Composable
@Preview(showBackground = true)
fun Home() {
    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF121212)) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (header, card, body) = createRefs()

            // Header
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
                            listOf(Color(0xFF1F1B24), Color(0xFF2A2730))
                        )
                    )
                    .padding(start = 16.dp, top = 32.dp)
            ) {
                Column {
                    Text("Expenso", fontSize = 24.sp, color = Color.White, fontWeight = FontWeight.ExtraBold)
                    Text("Track Your Expenses", fontSize = 14.sp, color = Color.Gray)
                }

                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 16.dp)
                )
            }

            // Expense Card
            ExpenseSummaryCard(
                balance = "₹5,000",
                totalSpent = "₹1,000",
                transactions = 5,
                modifier = Modifier.constrainAs(card) {
                    top.linkTo(header.bottom, margin = (-40).dp)
                    centerHorizontallyTo(parent)
                }
            )

            // Transactions
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .constrainAs(body) {
                        top.linkTo(card.bottom, margin = 24.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        height = Dimension.fillToConstraints
                    }
            ) {
                transactionList()
            }
        }
    }
}

@Composable
fun ExpenseSummaryCard(
    balance: String,
    totalSpent: String,
    transactions: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth(0.9f)
            .height(180.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2D2A37))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Text("Current Balance", color = Color(0xFFB0BEC5), fontSize = 14.sp)
            Text(balance, color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Total Spent", color = Color(0xFF90A4AE), fontSize = 12.sp)
                    Text(totalSpent, color = Color.White, fontSize = 16.sp)
                }

                Column {
                    Text("Transactions", color = Color(0xFF90A4AE), fontSize = 12.sp)
                    Text(transactions.toString(), color = Color.White, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun transactionList() {
    Column(modifier = Modifier.padding(horizontal = 8.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Recent Transactions", color = Color.White, fontSize = 18.sp)
            Text("See All", color = Color.Gray, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        transactionItem(
            title = "Groceries",
            amount = "- ₹750",
            icon = Icons.Default.CheckCircle,
            date = "Jun 3, 2025"
        )
        transactionItem(
            title = "Salary",
            amount = "+ ₹20,000",
            icon = Icons.Default.CheckCircle,
            date = "Jun 1, 2025"
        )
        transactionItem(
            title = "Electricity Bill",
            amount = "- ₹1,200",
            icon = Icons.Default.CheckCircle,
            date = "May 30, 2025"
        )
    }
}

@Composable
fun transactionItem(
    title: String,
    amount: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    date: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFF1E1E1E)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "$title icon",
                tint = Color(0xFFBB86FC)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
            Text(date, fontSize = 12.sp, color = Color.Gray)
        }

        Text(
            text = amount,
            fontSize = 14.sp,
            color = if (amount.startsWith("-")) Color(0xFFFF6F61) else Color(0xFF81C784),
            fontWeight = FontWeight.Bold
        )
    }
}

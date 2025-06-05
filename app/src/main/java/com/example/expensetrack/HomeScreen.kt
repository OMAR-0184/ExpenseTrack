package com.example.expensetrack

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.expensetrack.data.model.ExpenseEntity
import com.example.expensetrack.viewmodel.HomeViewModel
import com.example.expensetrack.viewmodel.HomeViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(onNavigateToAddExpense: () -> Unit) { // Added navigation lambda
    val viewModel: HomeViewModel = HomeViewModelFactory(LocalContext.current).create(HomeViewModel::class.java)
    val state = viewModel.allExpenses.collectAsState(initial = emptyList())

    val expenses = viewModel.getTotalExpense(state.value)
    val balance = viewModel.getBalance(state.value)
    val transactions = viewModel.getTransactions(state.value)

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddExpense,
                containerColor = Color(0xFFBB86FC), // Example FAB color
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, "Add new expense/income")
            }
        },
        containerColor = Color(0xFF121212) // Scaffold background color
    ) { paddingValues ->
        ConstraintLayout(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) { // Apply padding values to ConstraintLayout
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

            // Summary Card
            ExpenseSummaryCard(
                balance = balance,
                totalSpent = expenses,
                transactions = transactions,
                modifier = Modifier.constrainAs(card) {
                    top.linkTo(header.bottom, margin = (-40).dp)
                    centerHorizontallyTo(parent)
                }
            )

            // Transaction List
            transactionList(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp) // Adjusted padding
                    .constrainAs(body) {
                        top.linkTo(card.bottom, margin = 24.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom) // Ensure it fills remaining space
                        height = Dimension.fillToConstraints
                    },
                list = state.value
            )
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
            Text("₹$balance", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold) // Added Rupee symbol
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Total Spent", color = Color(0xFF90A4AE), fontSize = 12.sp)
                    Text("₹$totalSpent", color = Color.White, fontSize = 16.sp) // Added Rupee symbol
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
fun transactionList(modifier: Modifier, list: List<ExpenseEntity>) {
    LazyColumn(modifier = modifier) { // Removed unnecessary horizontal padding here as it's in the parent
        item {
            Box(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) { // Added bottom padding
                Text(
                    "Recent Transactions",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
            }
        }

        items(list) {
            transactionItem(
                title = it.title,
                amount = if (it.income > 0) "+ ₹${String.format("%.2f", it.income)}" else "- ₹${String.format("%.2f", it.expense)}", // Formatted amounts
                icon = Icons.Default.CheckCircle, // You might want dynamic icons based on category/type
                date = it.date
            )
        }
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
            color = if (amount.contains("-")) Color(0xFFFF6F61) else Color(0xFF81C784), // Check for '-' not `startsWith("-")`
            fontWeight = FontWeight.Bold
        )
    }
}
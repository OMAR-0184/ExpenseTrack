package com.example.expensetrack

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AddExpense() {
    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF121212)) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (header, form) = createRefs()

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
            ) {
                Text(
                    text = "Add Expense",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            DataForm(
                modifier = Modifier.constrainAs(form) {
                    top.linkTo(header.bottom, margin = 32.dp)
                    centerHorizontallyTo(parent)
                }
            )
        }
    }
}

@Composable
fun DataForm(modifier: Modifier) {
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(getTodayDate()) }
    var category by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            context,
            { _, year, month, day ->
                date = String.format("%02d/%02d/%04d", day, month + 1, year)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
        showDatePicker = false
    }

    Card(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2D2A37)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = "Expense Details",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                singleLine = true,
                colors = textFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = amount,
                onValueChange = {
                    if (it.matches(Regex("^\\d*\\.?\\d{0,2}$"))) amount = it
                },
                label = { Text("Amount") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = textFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = date,
                onValueChange = {},
                label = { Text("Date") },
                singleLine = true,
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Pick Date", tint = Color.White)
                    }
                },
                colors = textFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Category") },
                singleLine = true,
                colors = textFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    // Handle Add Expense logic here
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7C4DFF),
                    contentColor = Color.White
                )
            ) {
                Text("Add Expense", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun textFieldColors(): TextFieldColors {
    return TextFieldDefaults.outlinedTextFieldColors(
        focusedLabelColor = Color(0xFFBB86FC),
        unfocusedLabelColor = Color.Gray,
        cursorColor = Color(0xFFBB86FC),
        containerColor = Color(0xFF3A3A3A),
        focusedBorderColor = Color(0xFFBB86FC),
        unfocusedBorderColor = Color.Gray
    )
}

fun getTodayDate(): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return sdf.format(Date())
}

@Preview(showBackground = true)
@Composable
fun AddExpensePreview() {
    AddExpense()
}

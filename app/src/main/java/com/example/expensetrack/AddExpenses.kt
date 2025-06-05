package com.example.expensetrack

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import com.example.expensetrack.data.model.ExpenseEntity
import com.example.expensetrack.viewmodel.AddExpenseViewModel
import com.example.expensetrack.viewmodel.AddExpenseViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AddExpense(onBack: () -> Unit) { // Added onBack for navigation


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
                    top.linkTo(header.bottom, margin = (-16).dp)
                    centerHorizontallyTo(parent)
                },
                onAddExpense = onBack // Pass onBack to DataForm
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataForm(modifier: Modifier = Modifier, onAddExpense: () -> Unit) { // Added onAddExpense callback
    val viewModel: AddExpenseViewModel = AddExpenseViewModelFactory(LocalContext.current).create( AddExpenseViewModel::class.java)
    val scrollState = rememberScrollState()

    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(getTodayDate()) }
    var category by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    var type by remember { mutableStateOf("Expense") }  // default selection

    if (showDatePicker) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            context,
            { _, year, month, day ->
                // Date format dd/MM/yyyy
                date = String.format("%02d/%02d/%04d", day, month + 1, year)
                showDatePicker = false // Reset showDatePicker after selection
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Card(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .fillMaxWidth()
            .verticalScroll(scrollState),
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
                label = { Text("Title", color = Color.White) },
                singleLine = true,
                colors = textFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = amount,
                onValueChange = {
                    // Allow only digits and at most one decimal point, with up to 2 decimal places
                    val newText = it.replace(",", ".") // Replace commas with dots for decimal
                    if (newText.matches(Regex("^\\d*\\.?\\d{0,2}$")) || newText.isEmpty()) {
                        amount = newText
                    }
                },
                label = { Text("Amount", color = Color.White) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = textFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = date,
                onValueChange = {},
                label = { Text("Date", color = Color.White) },
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
                label = { Text("Category", color = Color.White) },
                singleLine = true,
                colors = textFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            val types = listOf("Income", "Expense")
            ExpandedDropDown(
                listOfItems = types,
                selectedItem = type,
                onItemSelected = { type = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            val buttonText = if (type == "Income") "Add Income" else "Add Expense"

            Button(
                onClick = {
                    if (title.isBlank() || amount.isBlank() || category.isBlank()) {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    } else {
                        val amountValue = amount.toDoubleOrNull()
                        if (amountValue == null || amountValue <= 0) {
                            Toast.makeText(context, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
                        } else {
                            val expenseEntity = if (type == "Income") {
                                ExpenseEntity(
                                    title = title,
                                    income = amountValue,
                                    expense = 0.0,
                                    date = date,
                                    category = category
                                )
                            } else {
                                ExpenseEntity(
                                    title = title,
                                    income = 0.0,
                                    expense = amountValue,
                                    date = date,
                                    category = category
                                )
                            }
                            viewModel.addExpense(expenseEntity)
                            Toast.makeText(context, "$type added!", Toast.LENGTH_SHORT).show()
                            onAddExpense() // Navigate back after adding
                        }
                    }
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
                Text(buttonText, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpandedDropDown(
    listOfItems: List<String>,
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedItem,
            onValueChange = {},
            readOnly = true,
            label = { Text("Type", color = Color.White) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth(), // Added fillMaxWidth
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedTextColor = Color.White,
                focusedLabelColor = Color(0xFFBB86FC),
                unfocusedLabelColor = Color.Gray,
                cursorColor = Color(0xFFBB86FC),
                containerColor = Color(0xFF3A3A3A),
                focusedBorderColor = Color(0xFFBB86FC),
                unfocusedBorderColor = Color.Gray
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            listOfItems.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item, color = Color.White) }, // Set text color
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

fun getTodayDate(): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return sdf.format(Date())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun textFieldColors(): TextFieldColors {
    return TextFieldDefaults.outlinedTextFieldColors(
        focusedTextColor = Color.White,
        focusedLabelColor = Color(0xFFBB86FC),
        unfocusedLabelColor = Color.Gray,
        cursorColor = Color(0xFFBB86FC),
        containerColor = Color(0xFF3A3A3A),
        focusedBorderColor = Color(0xFFBB86FC),
        unfocusedBorderColor = Color.Gray,
        disabledLabelColor = Color.Gray, // Added for read-only fields
        disabledBorderColor = Color.Gray,
        disabledTextColor = Color.White,
        disabledTrailingIconColor = Color.White
    )
}

@Preview(showBackground = true)
@Composable
fun AddExpensePreview() {
    AddExpense(onBack = {})
}
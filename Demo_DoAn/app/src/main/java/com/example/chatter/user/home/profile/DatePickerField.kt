package com.example.chatter.user.home.profile

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.util.Calendar

@Composable
fun DatePickerField(
    label: String,
    selectedDate: Calendar,
    onDateSelected: (Calendar) -> Unit
) {
    val context = LocalContext.current

    val formattedDate = "${selectedDate.get(Calendar.DAY_OF_MONTH)} tháng ${selectedDate.get(Calendar.MONTH) + 1} ${selectedDate.get(Calendar.YEAR)}"

    OutlinedTextField(
        value = formattedDate,
        onValueChange = {},
        readOnly = true,
        label = { Text(label) },
        modifier = androidx.compose.ui.Modifier.clickable {
            showDatePicker(context, selectedDate) { newCalendar ->
                onDateSelected(newCalendar)
            }
        }
    )
}

// Hàm hiển thị DatePickerDialog, nhận Calendar làm đối số
fun showDatePicker(context: Context, initialDate: Calendar, onDatePicked: (Calendar) -> Unit) {
    val year = initialDate.get(Calendar.YEAR)
    val month = initialDate.get(Calendar.MONTH)
    val day = initialDate.get(Calendar.DAY_OF_MONTH)

    DatePickerDialog(context, { _: DatePicker, y: Int, m: Int, d: Int ->
        val selected = Calendar.getInstance().apply {
            set(y, m, d)
        }
        onDatePicked(selected)
    }, year, month, day).show()
}

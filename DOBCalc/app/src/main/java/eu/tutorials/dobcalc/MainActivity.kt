package eu.tutorials.dobcalc

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

  private var tvSelectedDate : TextView? = null
  private var tvAgeInMinutes : TextView? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val btnDatePicker: Button = findViewById(R.id.btnDatePicker)
    tvSelectedDate = findViewById(R.id.tvSelectedDate)
    tvAgeInMinutes = findViewById(R.id.tvAgeInMinutes)
    btnDatePicker.setOnClickListener {
      clickDatePicker()
    }
  }

  private fun clickDatePicker() {
    val myCalendar = Calendar.getInstance()
    val year = myCalendar.get(Calendar.YEAR)
    val month = myCalendar.get(Calendar.MONTH)
    val dayOfMonth = myCalendar.get(Calendar.DAY_OF_MONTH)

    val dpd = DatePickerDialog(
      this,
      { _view, _year, _month, _dayOfMonth ->
        Toast.makeText(this, "Year was $_year, month was ${_month + 1}, day was $_dayOfMonth", Toast.LENGTH_LONG).show()

        val selectedDate = "$_dayOfMonth/${_month + 1}/$_year"
        tvSelectedDate?.text = selectedDate

        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.KOREA)
        val theDate = sdf.parse(selectedDate)

        theDate?.let {
          val selectedDateInMinutes = theDate.time / 60000
          val currentDate = sdf.parse(sdf.format(System.currentTimeMillis()))
          currentDate?.let {
            val currentDateInMinutes = currentDate.time / 60000
            val differenceInMinutes = currentDateInMinutes - selectedDateInMinutes
            tvAgeInMinutes?.text = differenceInMinutes.toString()
          }
        }
      }, year, month, dayOfMonth
    )

    dpd.datePicker.maxDate = System.currentTimeMillis() - 86400000
    dpd.show()
  }

}
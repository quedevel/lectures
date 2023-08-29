package eu.tutorials.mycalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import java.lang.ArithmeticException

class MainActivity : AppCompatActivity() {

  private var tvInput: TextView? = null
  private var lastNumeric: Boolean = false
  private var lastDot: Boolean = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    tvInput = findViewById(R.id.tvInput)
  }

  fun onDigit(view: View) {
    tvInput?.append((view as Button).text)
    lastNumeric = true
    lastDot = false
  }

  fun onClear(view: View) {
    tvInput?.text = ""
  }

  fun onDecimalPoint(view: View) {
    if (lastNumeric && !lastDot) {
      tvInput?.append(".")
      lastNumeric = false
      lastDot = true
    }
  }

  fun onOperator(view: View) {
    tvInput?.text?.let {
      if (lastNumeric && !isOperatorAdded(it.toString())) {
        tvInput?.append((view as Button).text)
        lastNumeric = false
        lastDot = false
      }
    }
  }

  fun onEqual(view: View) {
    if (lastNumeric) {
      var tvValue = tvInput?.text.toString()
      var prefix = ""
      if (tvValue.startsWith("-")) {
        prefix = "-"
        tvValue = tvValue.substring(1)
      }
      try {
        val result = when {
          tvValue.contains("-") -> performCalculation(tvValue, "-", prefix) { a, b -> a - b }
          tvValue.contains("+") -> performCalculation(tvValue, "+", prefix) { a, b -> a + b }
          tvValue.contains("*") -> performCalculation(tvValue, "*", prefix) { a, b -> a * b }
          tvValue.contains("/") -> performCalculation(tvValue, "/", prefix) { a, b -> a / b }
          else -> return
        }

        tvInput?.text = removeZeroAfterDot(result.toString())
      } catch (e: ArithmeticException) {
        e.printStackTrace() // logcat
      }
    }
  }

  private fun performCalculation(
    input: String,
    operator: String,
    prefix: String,
    operation: (Double, Double) -> Double
  ): Double {
    val splitValue = input.split(operator)
    var one = splitValue[0]
    var two = splitValue[1]

    if (prefix.isNotEmpty()) {
      one = prefix + one
    }

    return operation(one.toDouble(), two.toDouble())
  }

  private fun removeZeroAfterDot(result: String): String {
    return if(result.contains(".0")) result.substring(0, result.length - 2) else result
  }

  private fun isOperatorAdded(value: String): Boolean {
    return if (value.startsWith("-")) {
      false
    } else {
      value.contains("/")
          || value.contains("*")
          || value.contains("+")
          || value.contains("-")
    }
  }
}
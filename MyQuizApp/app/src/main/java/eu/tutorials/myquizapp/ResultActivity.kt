package eu.tutorials.myquizapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class ResultActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_result)

    val tvName: TextView = findViewById(R.id.tvName)
    val tvScore: TextView = findViewById(R.id.tvScore)
    val btnFinish: Button = findViewById(R.id.btnFinish)

    tvName.text = intent.getStringExtra(Constants.USER_NAME)

    val total = intent.getIntExtra(Constants.TOTAL_QUESTION, 0)
    val correct = intent.getIntExtra(Constants.CORRECT_ANSWERS, 0)

    tvScore.text = "Your Score is $correct out of $total"

    btnFinish.setOnClickListener {
      startActivity(Intent(this, MainActivity::class.java))
      finish()
    }
  }
}
package eu.tutorials.myquizapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat

class QuizQuestionsActivity : AppCompatActivity() {

  private var mCurrentPosition: Int = 1
  private var mQuestions: ArrayList<Question>? = null
  private var mSelectedOptionPosition: Int = 0
  private var mUsername: String? = null
  private var mCorrectAnswers: Int = 0

  private var progressBar: ProgressBar? = null
  private var tvProgress: TextView? = null
  private var tvQuestion: TextView? = null
  private var ivImage: ImageView? = null
  private var tvOptionOne: TextView? = null
  private var tvOptionTwo: TextView? = null
  private var tvOptionThree: TextView? = null
  private var tvOptionFour: TextView? = null
  private var btnSubMit: Button? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_quiz_questions)

    mUsername = intent.getStringExtra(Constants.USER_NAME)

    progressBar = findViewById(R.id.progressBar)
    tvProgress = findViewById(R.id.tvProgress)
    tvQuestion = findViewById(R.id.tvQuestion)
    ivImage = findViewById(R.id.ivImage)
    tvOptionOne = findViewById(R.id.tvOptionOne)
    tvOptionTwo = findViewById(R.id.tvOptionTwo)
    tvOptionThree = findViewById(R.id.tvOptionThree)
    tvOptionFour = findViewById(R.id.tvOptionFour)
    btnSubMit = findViewById(R.id.btnSubmit)

    tvOptionOne?.setOnClickListener { onClick(tvOptionOne) }
    tvOptionTwo?.setOnClickListener { onClick(tvOptionTwo) }
    tvOptionThree?.setOnClickListener { onClick(tvOptionThree) }
    tvOptionFour?.setOnClickListener { onClick(tvOptionFour) }
    btnSubMit?.setOnClickListener { onClick(btnSubMit) }

    mQuestions = Constants.getQuestions()

    setQuestions()

  }

  private fun setQuestions() {
    defaultOptionsView()
    val question: Question = mQuestions!![mCurrentPosition - 1]
    ivImage?.setImageResource(question.image)
    progressBar?.progress = mCurrentPosition
    tvProgress?.text = "$mCurrentPosition / ${progressBar?.max}"
    tvQuestion?.text = question.question
    tvOptionOne?.text = question.optionOne
    tvOptionTwo?.text = question.optionTwo
    tvOptionThree?.text = question.optionThree
    tvOptionFour?.text = question.optionFour

    if (mCurrentPosition == mQuestions!!.size) {
      btnSubMit?.text = "FINISH"
    } else {
      btnSubMit?.text = "SUBMIT"
    }
  }

  private fun defaultOptionsView() {
    val options = ArrayList<TextView>()
    tvOptionOne?.let {
      options.add(0, it)
    }
    tvOptionTwo?.let {
      options.add(1, it)
    }
    tvOptionThree?.let {
      options.add(2, it)
    }
    tvOptionFour?.let {
      options.add(3, it)
    }

    for (option in options) {
      option.setTextColor(Color.parseColor("#7A8089"))
      option.typeface = Typeface.DEFAULT
      option.background = ContextCompat.getDrawable(this, R.drawable.default_option_border_bg)
    }
  }

  private fun selectedOptionView(tv: TextView, selectedOptionNum: Int) {
    defaultOptionsView()

    mSelectedOptionPosition = selectedOptionNum

    tv.setTextColor(Color.parseColor("#363A43"))
    tv.setTypeface(tv.typeface, Typeface.BOLD)
    tv.background = ContextCompat.getDrawable(this, R.drawable.selected_option_border_bg)
  }

  fun onClick(view: View?) {
    when (view?.id) {
      R.id.tvOptionOne -> {
        tvOptionOne?.let {
          selectedOptionView(it, 1)
        }
      }

      R.id.tvOptionTwo -> {
        tvOptionTwo?.let {
          selectedOptionView(it, 2)
        }
      }

      R.id.tvOptionThree -> {
        tvOptionThree?.let {
          selectedOptionView(it, 3)
        }
      }

      R.id.tvOptionFour -> {
        tvOptionFour?.let {
          selectedOptionView(it, 4)
        }
      }

      R.id.btnSubmit -> {
        if (mSelectedOptionPosition == 0) {
          mCurrentPosition++
          when {
            mCurrentPosition <= mQuestions!!.size -> setQuestions()
            else -> {
              val intent = Intent(this, ResultActivity::class.java)
              intent.putExtra(Constants.USER_NAME, mUsername)
              intent.putExtra(Constants.TOTAL_QUESTION, mQuestions!!.size)
              intent.putExtra(Constants.CORRECT_ANSWERS, mCorrectAnswers)
              startActivity(intent)
              finish()
            }
          }

        } else {
          val question = mQuestions?.get(mCurrentPosition - 1)
          if (question!!.correctAnswer != mSelectedOptionPosition) {
            answerView(mSelectedOptionPosition, R.drawable.wrong_option_border_bg)
          } else {
            mCorrectAnswers++
          }
          answerView(question.correctAnswer, R.drawable.correct_option_border_bg)

          if (mCurrentPosition == mQuestions!!.size) {
            btnSubMit?.text = "FINISH"
          } else {
            btnSubMit?.text = "GO TO NEXT QUESTION"
          }

          mSelectedOptionPosition = 0
        }
      }
    }
  }

  private fun answerView(answer: Int, drawableView: Int) {
    when (answer) {
      1 -> tvOptionOne?.background = ContextCompat.getDrawable(this, drawableView)
      2 -> tvOptionTwo?.background = ContextCompat.getDrawable(this, drawableView)
      3 -> tvOptionThree?.background = ContextCompat.getDrawable(this, drawableView)
      4 -> tvOptionFour?.background = ContextCompat.getDrawable(this, drawableView)
    }
  }
}
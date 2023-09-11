package com.peachmind.chatapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.peachmind.chatapp.Key.Companion.DB_USERS
import com.peachmind.chatapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
  private lateinit var binding: ActivityLoginBinding
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityLoginBinding.inflate(layoutInflater)
    setContentView(binding.root)

    binding.signUpButton.setOnClickListener { signUp() }
    binding.signInButton.setOnClickListener { signIn() }
  }

  /**
   * 로그인
   */
  private fun signIn() {
    val email = binding.emailEditText.text.toString()
    val password = binding.passwordEditText.text.toString()

    if (email.isEmpty() || password.isEmpty()) {
      Toast.makeText(this@LoginActivity, "이메일 또는 패스워드가 입력되지 않았습니다.", Toast.LENGTH_SHORT).show()
      return
    }

    Firebase.auth.signInWithEmailAndPassword(email, password)
      .addOnCompleteListener(this) { task ->
        val currentUser = Firebase.auth.currentUser
        if (task.isSuccessful && currentUser != null) {
          val userId = currentUser.uid

          Firebase.messaging.token.addOnCompleteListener {
            val token = it.result
            val user = mutableMapOf<String, Any>()
            user["userId"] = userId
            user["username"] = email
            user["fcmToken"] = token

            Firebase.database.reference.child(DB_USERS).child(userId).updateChildren(user)

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
          }.addOnFailureListener {
            it.printStackTrace()
          }
        } else {
          Log.e("LoginActivity", task.exception.toString())
          Toast.makeText(this@LoginActivity, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show()
        }
      }
  }

  /**
   * 회원 가입
   */
  private fun signUp() {
    val email = binding.emailEditText.text.toString()
    val password = binding.passwordEditText.text.toString()

    if (email.isEmpty() || password.isEmpty()) {
      Toast.makeText(this@LoginActivity, "이메일 또는 패스워드가 입력되지 않았습니다.", Toast.LENGTH_SHORT).show()
      return
    }

    Firebase.auth.createUserWithEmailAndPassword(email, password)
      .addOnCompleteListener(this) { task ->
        if (task.isSuccessful) {
          Toast.makeText(this@LoginActivity, "회원가입에 성공했습니다. 로그인해주세요.", Toast.LENGTH_SHORT).show()
        } else {
          Toast.makeText(this@LoginActivity, "회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show()
        }
      }
  }
}
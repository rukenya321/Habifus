package com.example.habifus

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.activity.enableEdgeToEdge
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)

        val signInTextView: TextView = findViewById(R.id.alreadyhaveanaccount)
        val signInText = "Already have an account? Sign in"
        val spannableString = SpannableString(signInText)

        val signInClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        val signInStartIndex = signInText.indexOf("Sign in")
        val signInEndIndex = signInStartIndex + "Sign in".length

        spannableString.setSpan(
            signInClickableSpan,
            signInStartIndex,
            signInEndIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        signInTextView.text = spannableString
        signInTextView.movementMethod = LinkMovementMethod.getInstance()
        signInTextView.highlightColor = getColor(android.R.color.transparent)

        val fullnameEditText: EditText = findViewById(R.id.fullName)
        val emailEditText: EditText = findViewById(R.id.email)
        val passwordEditText: EditText = findViewById(R.id.password)
        val signUpButton: Button = findViewById(R.id.button)

        signUpButton.setOnClickListener {
            val fullName = fullnameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                performSignUp(fullName, email, password)
            }
        }
    }

    private fun performSignUp(fullName: String, email: String, password: String) {
        val client = OkHttpClient()
        val json = JSONObject().apply {
            put("fullName", fullName)
            put("email", email)
            put("password", password)
        }

        val requestBody = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            json.toString()
        )
        val request = Request.Builder()
            .url("http://habifus.scienceontheweb.net/signup.php")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@SignUpActivity, "Network Error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                if (response.isSuccessful && responseBody != null) {
                    try {
                        val jsonResponse = JSONObject(responseBody)
                        if (jsonResponse.getString("status") == "success") {
                            runOnUiThread {
                                Toast.makeText(
                                    this@SignUpActivity,
                                    "Signup Successful",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        } else {
                            runOnUiThread {
                                Toast.makeText(
                                    this@SignUpActivity,
                                    "Signup Failed: ${jsonResponse.getString("message")}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } catch (e: Exception) {
                        runOnUiThread {
                            Toast.makeText(
                                this@SignUpActivity,
                                "Invalid response from server",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@SignUpActivity, "Server Error", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        })
    }
}

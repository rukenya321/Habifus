package com.example.habifus

import android.os.Bundle
import android.content.Intent
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.IOException

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val signInTextView: TextView = findViewById(R.id.alredyhaveanaccount)
        val signInText = "Already have an account? Sign in"
        val spannableString = SpannableString(signInText)

        val signInClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // Handle sign in click
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

        //UI Components
        val fullnameEditText: EditText = findViewById(R.id.fullName)
        val emailEditText: EditText = findViewById(R.id.email)
        val passwordEditText: EditText = findViewById(R.id.passworD)
        val signUpButton: Button = findViewById(R.id.button)

        // Set click listener for the sign up button
        signUpButton.setOnClickListener {
            val fullName = fullnameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // Perform sign up validation and sign up logic here
            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                // Perform sign up logic here
                performSignUp(fullName, email, password)
            }
        }
    }

    // Function to perform sign up logic
    private fun performSignUp(fullName: String, email: String, password: String) {
        val client = OkHttpClient()

        val json = JSONObject().apply {
            put("fullName", fullName)
            put("email", email)
            put("password", password)
        }

        val requestBody = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), json.toString())
        val request = Request.Builder()
            .url("http://sql7.freemysqlhosting.net/signup.php")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@SignUpActivity, "Network Error", Toast.LENGHT_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
               if (response.isSuccessful) {
                   val responseBody = response.body?.string()
                   val jsonResponse = JSONObject(responseBody)

                   if (jsonResponse.getString("status") == "success") {
                       runOnUiThread {
                           Toast.makeText(this@SignUpActivity, "Signup Successful", Toast.LENGHT_SHORT).show()
                           val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                           startActivity(intent)
                           finish()
                       }
                   } else {
                       runOnUiThread {
                           Toast.makeText(this@SignUpActivity, "Signup Failed", Toast.LENGHT_SHORT).show()
                       }
                   }
               }
            }
        })

    }
}
/*package com.example.habifus

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
            put("name", fullName) // Make sure the key names match those expected by the server
            put("email", email)
            put("password", password)
        }

        val requestBody = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            json.toString()
        )
        val request = Request.Builder()
            .url("http://www.habifus.byethost7.com/signup.php")
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
                runOnUiThread {
                    if (response.isSuccessful && responseBody != null) {
                        try {
                            val jsonResponse = JSONObject(responseBody)
                            if (jsonResponse.getString("status") == "success") {
                                Toast.makeText(
                                    this@SignUpActivity,
                                    "Signup Successful",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(
                                    this@SignUpActivity,
                                    "Signup Failed: ${jsonResponse.getString("message")}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Toast.makeText(
                                this@SignUpActivity,
                                "Invalid response from server",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(this@SignUpActivity, "Server Error", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        })
    }
}*/
package com.example.habifus

import android.content.Intent
import android.os.Bundle
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
import androidx.core.content.ContextCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.gson.Gson

class SignUpActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)

        val window = window
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar_color)

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
                checkEmailExists(fullName, email, password)
            }
        }
    }

    private fun checkEmailExists(fullName: String, email: String, password: String) {
        RetrofitClient.apiService.checkEmailExists(email).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val gson = Gson()
                        val emailCheckResponse = gson.fromJson(responseBody, EmailCheckResponse::class.java)
                        if (!emailCheckResponse.exists) {
                            performSignup(fullName, email, password)
                        } else {
                            Toast.makeText(this@SignUpActivity, "Email already exists. Please use a different email.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@SignUpActivity, "Unexpected response format", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@SignUpActivity, "Server Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(this@SignUpActivity, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun performSignup(fullName: String, email: String, password: String) {
        val request = SignUpRequest(fullName, email, password)
        RetrofitClient.apiService.signUp(request).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        try {
                            val gson = Gson()
                            val signupResponse = gson.fromJson(responseBody, SignUpResponse::class.java)
                            if (signupResponse.status == "success") {
                                Toast.makeText(this@SignUpActivity, "Signup Successful", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this@SignUpActivity, "Error: ${signupResponse.message}", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(this@SignUpActivity, "Malformed JSON", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@SignUpActivity, "Server Error: Empty Response", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@SignUpActivity, "Server Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(this@SignUpActivity, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }





}


/*package com.example.habifus

import android.content.Intent
import android.os.Bundle
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
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
                performSignup(email, fullName, password)
            }
        }
    }

    private fun performSignup(email: String, fullName: String, password: String) {
        val request = SignUpRequest(email, fullName, password)
        RetrofitClient.apiService.signUp(request).enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                if (response.isSuccessful) {
                    val signupResponse = response.body()
                    if (signupResponse != null) {
                        if (signupResponse.status == "success") {
                            Toast.makeText(this@SignUpActivity, "Signup Successful", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@SignUpActivity, "Error: ${signupResponse.message}", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@SignUpActivity, "Server Error: Empty Response", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@SignUpActivity, "Server Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                Toast.makeText(this@SignUpActivity, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}*/



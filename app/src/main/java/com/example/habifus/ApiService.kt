/*package com.example.habifus

// ApiService.kt
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class SignUpRequest(val fullName: String, val email: String, val password: String)
data class SignUpResponse(val status: String, val message: String)

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val status: String, val message: String)

interface ApiService {
    @POST("signup.php")
    fun signUp(@Body request: SignUpRequest): Call<String>  // Changed to String

    @POST("login.php")
    fun login(@Body request: LoginRequest): Call<String>  // Changed to String
}*/


package com.example.habifus

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

data class SignUpRequest(val fullName: String, val email: String, val password: String)
data class SignUpResponse(val status: String, val message: String, val userId: Int)

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val status: String, val message: String, val userId: Int, val fullName: String)

data class EmailCheckResponse(val exists: Boolean)

data class ForgotPasswordRequest(val email: String)



data class TaskRequest(
    val userId: Int,
    val title: String,
    val description: String,
    val dueDate: String
)

data class TaskResponse(
    val status: String,
    val message: String
)

interface ApiService {
    @POST("signup.php")
    fun signUp(@Body request: SignUpRequest): Call<String>  // Changed to String

    @POST("login.php")
    fun login(@Body request: LoginRequest): Call<String>  // Changed to String

    @POST("add_task.php")
    fun addTask(@Body request: TaskRequest): Call<String>

    @GET("get_tasks.php")
    fun getTasks(@Query("userId") userId: Int): Call<String>

    @GET("check_email.php")
    fun checkEmailExists(@Query("email") email: String): Call<String>

    @DELETE("delete_user.php")
    fun deleteUser(@Query("id") userId: Int): Call<String>

    @POST("reset_password.php")
    fun forgotPassword(@Body request: ForgotPasswordRequest): Call<String>
}

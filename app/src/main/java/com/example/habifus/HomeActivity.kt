/*package com.example.habifus

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeActivity : AppCompatActivity() {

    private lateinit var adapter: TaskAdapter
    private val tasks = mutableListOf<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        // An example of the username
        val userName = "Rukenya Munene"

        // Showcase the welcome message
        val welcomeTextView: TextView = findViewById(R.id.welcomeTextView)
        welcomeTextView.text = "Welcome, $userName!"

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val fab: FloatingActionButton = findViewById(R.id.fab)

        adapter = TaskAdapter(tasks)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        fab.setOnClickListener {
            showAddTaskDialog()
        }
    }

    private fun showAddTaskDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_task, null)
        val taskTitleEditText: EditText = dialogView.findViewById(R.id.taskTitleEditText)
        val taskDescriptionEditText: EditText =
            dialogView.findViewById(R.id.taskDescriptionEditText)
        val taskDueDateEditText: EditText = dialogView.findViewById(R.id.taskDueDateEditText)
        val cancelButton: Button = dialogView.findViewById(R.id.cancelButton)
        val addButton: Button = dialogView.findViewById(R.id.addButton)

        val alertDialog = AlertDialog.Builder(this, R.style.CustomDialogTheme)
            .setView(dialogView)
            .create()

        cancelButton.setOnClickListener {
            alertDialog.dismiss()
        }

        addButton.setOnClickListener {
            val title = taskTitleEditText.text.toString()
            val description = taskDescriptionEditText.text.toString()
            val dueDate = taskDueDateEditText.text.toString()

            if (title.isNotEmpty() && description.isNotEmpty() && dueDate.isNotEmpty()) {
                val newTask = Task(title, description, dueDate)
                tasks.add(newTask)
                adapter.notifyItemInserted(tasks.size - 1)
                alertDialog.dismiss()
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        alertDialog.show()
    }
}*/


package com.example.habifus

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class HomeActivity : AppCompatActivity() {

    private lateinit var adapter: TaskAdapter
    private val tasks = mutableListOf<Task>()
    private var userId: Int = -1
    private lateinit var fullName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val window = window
        window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar_color)

        // Retrieve userId from intent
        userId = intent.getIntExtra("userId", -1)
        fullName = intent.getStringExtra("fullName") ?: "User"

        // Display welcome message
        val welcomeTextView: TextView = findViewById(R.id.welcomeTextView)
        welcomeTextView.text = "Welcome, $fullName!"

        val deleteIcon: ImageView = findViewById(R.id.deleteIcon)
        deleteIcon.setOnClickListener {
            showDeleteAccountDialog()
        }

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val fab: FloatingActionButton = findViewById(R.id.fab)

        adapter = TaskAdapter(tasks) { task, isChecked ->
            task.done = isChecked
            // Optionally, update the task status on the server
            updateTaskStatus(task)

        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        fab.setOnClickListener {
            showAddTaskDialog()
        }

        if (userId != -1){
            loadTasks()
        }
        else {
            Toast.makeText(this, "Invalid user ID", Toast.LENGTH_SHORT).show()
        }

    }


    private fun showDeleteAccountDialog() {
        AlertDialog.Builder(this)
            .setTitle("Delete Account")
            .setMessage("Are you sure you want to delete your account? This action cannot be undone.")
            .setPositiveButton("Delete") { dialog, which ->
                deleteAccount()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteAccount() {
        RetrofitClient.apiService.deleteUser(userId).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@HomeActivity, "Account deleted successfully", Toast.LENGTH_SHORT).show()
                    // Optionally, finish the activity and navigate to the login screen
                    finish()
                } else {
                    Toast.makeText(this@HomeActivity, "Failed to delete account: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(this@HomeActivity, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateTaskStatus(task: Task) {
        // Implement the logic to update the task status on the server
        Toast.makeText(this, "Task status updated", Toast.LENGTH_SHORT).show()
    }

    private fun showAddTaskDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_task, null)
        val taskTitleEditText: EditText = dialogView.findViewById(R.id.taskTitleEditText)
        val taskDescriptionEditText: EditText = dialogView.findViewById(R.id.taskDescriptionEditText)
        val taskDueDateEditText: EditText = dialogView.findViewById(R.id.taskDueDateEditText)
        val cancelButton: Button = dialogView.findViewById(R.id.cancelButton)
        val addButton: Button = dialogView.findViewById(R.id.addButton)

        val alertDialog = AlertDialog.Builder(this, R.style.CustomDialogTheme)
            .setView(dialogView)
            .create()

        cancelButton.setOnClickListener {
            alertDialog.dismiss()
        }

        addButton.setOnClickListener {
            val title = taskTitleEditText.text.toString()
            val description = taskDescriptionEditText.text.toString()
            val dueDate = taskDueDateEditText.text.toString()

            if (title.isNotEmpty() && description.isNotEmpty() && dueDate.isNotEmpty()) {
                val newTask = Task(title, description, dueDate)
                tasks.add(newTask)
                adapter.notifyItemInserted(tasks.size - 1)
                saveTask(newTask)
                alertDialog.dismiss()
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        taskDueDateEditText.setOnClickListener {
            showDateTimePicker(taskDueDateEditText)
        }

        alertDialog.show()
    }

    private fun showDateTimePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(this, { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            TimePickerDialog(this, { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                editText.setText("${year}-${month + 1}-${dayOfMonth} ${hourOfDay}:${minute}")
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }


    private fun saveTask(task: Task) {
        val request = TaskRequest(userId, task.title, task.description, task.dueDate)
        RetrofitClient.apiService.addTask(request).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (!responseBody.isNullOrEmpty()) {
                        Toast.makeText(this@HomeActivity, "Task saved successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@HomeActivity, "Server Error: Empty Response", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@HomeActivity, "Server Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(this@HomeActivity, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun loadTasks() {
        RetrofitClient.apiService.getTasks(userId).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        try {
                            // Parse the JSON response into a list of tasks
                            val taskListType = object : TypeToken<List<Task>>() {}.type
                            val taskList = Gson().fromJson<List<Task>>(responseBody, taskListType)
                            tasks.clear()
                            tasks.addAll(taskList)
                            adapter.notifyDataSetChanged()
                        } catch (e: JsonSyntaxException) {
                            Toast.makeText(this@HomeActivity, "Error parsing JSON", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@HomeActivity, "Server Error: Empty Response", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    handleErrorResponse(response)
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(this@HomeActivity, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun handleErrorResponse(response: Response<*>) {
        try {
            val errorResponse = response.errorBody()?.string()
            // Assuming the error response is in the form {"status":"error","message":"Missing user ID."}
            val jsonResponse = JSONObject(errorResponse)
            val errorMessage = jsonResponse.getString("message")
            Toast.makeText(this@HomeActivity, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this@HomeActivity, "Unexpected error: ${response.code()}", Toast.LENGTH_SHORT).show()
        }
    }
}
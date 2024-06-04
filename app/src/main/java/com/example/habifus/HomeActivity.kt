/*package com.example.habifus

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val fab: FloatingActionButton = findViewById(R.id.fab)

        val tasks  = listOf(
            Task("Pay Rent", "In 1 hour and 10 minutes", "06 June"),
            Task("Meeting with Clients", "In 2 hours and 25 minutes", "07 June"),
            Task("Buy Some Food", "Time to do this activity", "08 June"),
            Task("Appointment", "In 1 hour and 10 minutes", "09 June"),
            Task("Visit Grandmother", "In 2 hours and 30 minutes", "10 June"),
            Task("Shopping", "In 3 hours and 05 minutes", "11 June")
        )

        val adapter = TaskAdapter(tasks)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        fab.setOnClickListener {
            // Handle FAB click event to add a new task
        }
    }
}*/


package com.example.habifus

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

        // Hardcoded user name for the example
        val userName = "John Doe"

        // Display welcome message
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
}
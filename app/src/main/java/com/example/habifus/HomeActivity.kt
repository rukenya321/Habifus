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
}

package com.example.habifus

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

        val tasks = listOf(
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
}
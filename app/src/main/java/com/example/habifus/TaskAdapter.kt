/*package com.example.habifus

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class Task(val title: String, val description: String, val dueDate: String, var done: Boolean = false)

class TaskAdapter(private val tasks: List<Task>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.taskTitle.text = task.title
        holder.taskDescription.text = task.description
        holder.taskDueDate.text = task.dueDate
        holder.taskDoneCheckBox.isChecked = task.done

        taskDoneCheckBox.setOnCheckedChangeListener { _, isChecked ->
            onTaskCheckedChange(task, isChecked)
        }
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskTitle: TextView = itemView.findViewById(R.id.taskTitle)
        val taskDescription: TextView = itemView.findViewById(R.id.taskDescription)
        val taskDueDate: TextView = itemView.findViewById(R.id.taskDueDate)
        val taskReminder: ImageView = itemView.findViewById(R.id.taskReminder)
        private val taskDoneCheckBox: CheckBox = itemView.findViewById(R.id.taskDoneCheckBox)
    }
}*/


package com.example.habifus

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class Task(val title: String, val description: String, val dueDate: String, var done: Boolean = false)

class TaskAdapter(private val tasks: List<Task>, private val onTaskCheckedChange: (Task, Boolean) -> Unit) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task)
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val taskTitle: TextView = itemView.findViewById(R.id.taskTitle)
        private val taskDescription: TextView = itemView.findViewById(R.id.taskDescription)
        private val taskDueDate: TextView = itemView.findViewById(R.id.taskDueDate)
        private val taskDoneCheckBox: CheckBox = itemView.findViewById(R.id.taskDoneCheckBox)

        fun bind(task: Task) {
            taskTitle.text = task.title
            taskDescription.text = task.description
            taskDueDate.text = task.dueDate
            taskDoneCheckBox.isChecked = task.done

            taskDoneCheckBox.setOnCheckedChangeListener { _, isChecked ->
                onTaskCheckedChange(task, isChecked)
            }
        }
    }
}

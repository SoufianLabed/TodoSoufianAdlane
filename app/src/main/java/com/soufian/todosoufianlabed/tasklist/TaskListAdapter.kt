package com.soufian.todosoufianlabed.tasklist

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.soufian.todosoufianlabed.R
import java.util.*

object TasksDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Task, newItem: Task) = oldItem == newItem
}

class TaskListAdapter() : ListAdapter<Task, TaskListAdapter.TaskViewHolder>(TasksDiffCallback) {


    var onDeleteTask: ((Task) -> Unit)? = null
    var onEditTask: ((Task) -> Unit)? = null
    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(taskTitle: Task) {
            itemView.apply { // `apply {}` permet d'éviter de répéter `itemView.*`
                // TODO: afficher les données et attacher les listeners aux différentes vues de notre [itemView]
                val myAwesomeTextView = findViewById(R.id.task_title) as TextView
                myAwesomeTextView.setText(taskTitle.title)

                val myDescTextView = findViewById(R.id.task_description) as TextView
                myDescTextView.setText(taskTitle.description)

                val mydelete = findViewById(R.id.imageButton) as ImageButton
                mydelete.setOnClickListener {
                    onDeleteTask?.invoke(taskTitle)
                }

                val myEdit  =  findViewById(R.id.EditB) as ImageButton

                myEdit.setOnClickListener {

                    onEditTask?.invoke(taskTitle)
                }



                    /*
                            taskList.add(Task(id = UUID.randomUUID().toString(), title = "Task ${taskList.size + 1}"))
                            Log.d("TAG", taskList.size.toString())
                            recyclerView.adapter?.notifyDataSetChanged()*/




            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_task,
            parent,
            false
        )

        return TaskViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(currentList[position])
    }


}
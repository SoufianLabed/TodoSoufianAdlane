package com.soufian.todosoufianlabed.task

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.soufian.todosoufianlabed.R
import com.soufian.todosoufianlabed.tasklist.Task
import java.util.*

class TaskActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.task)

        val fab = findViewById<FloatingActionButton>(R.id.floatingActionButton2)
        val title2 = findViewById<EditText>(R.id.task_title2)
        val description2 = findViewById<EditText>(R.id.task_description2)
        val task = intent.getSerializableExtra("Task") as? Task


        title2.setText(task?.title)
        description2.setText(task?.description)

        fab.setOnClickListener {

           val newTask = Task(id = task?.id
                    ?: UUID.randomUUID().toString(), title = title2.text.toString(), description = description2.text.toString())

            Log.d("desc",description2.text.toString())
            intent.putExtra("Task", newTask)


// description2.text.toString()
            setResult(RESULT_OK, intent)
            finish()
        }


    }

    companion object {
        const val ADD_TASK_REQUEST_CODE = 666
    }

}
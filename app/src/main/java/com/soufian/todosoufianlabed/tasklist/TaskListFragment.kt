package com.soufian.todosoufianlabed.tasklist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.soufian.todosoufianlabed.R
import com.soufian.todosoufianlabed.TaskListViewModel
import com.soufian.todosoufianlabed.network.Api
import com.soufian.todosoufianlabed.network.TasksRepository
import com.soufian.todosoufianlabed.task.TaskActivity
import com.soufian.todosoufianlabed.task.TaskActivity.Companion.ADD_TASK_REQUEST_CODE
import kotlinx.coroutines.launch
import java.util.*

class TaskListFragment : Fragment() {


    val adapter = TaskListAdapter()

    private val tasksRepository = TasksRepository()
    private val viewModel: TaskListViewModel by viewModels()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_task_list, container, false)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)

        recyclerView.adapter = adapter

        val myButton  =  view.findViewById(R.id.floatingActionButton) as FloatingActionButton
        val myEdit  =  view.findViewById(R.id.floatingActionButton) as FloatingActionButton



        viewModel.taskList.observe(viewLifecycleOwner) { newList ->
            // utliser la liste

            adapter.submitList(newList)


        }




        myButton.setOnClickListener({

            val intent = Intent(activity, TaskActivity::class.java)
            startActivityForResult(intent, ADD_TASK_REQUEST_CODE)


    /*
            taskList.add(Task(id = UUID.randomUUID().toString(), title = "Task ${taskList.size + 1}"))
            Log.d("TAG", taskList.size.toString())
            recyclerView.adapter?.notifyDataSetChanged()*/

        })


        recyclerView.adapter = adapter
        /*adapter.onDeleteTask = { task ->

            taskList.remove(task)
            adapter.notifyDataSetChanged()
        }*/


        adapter.onDeleteTask = { task ->


            viewModel.deleteTask(task)
            adapter.notifyDataSetChanged()

        }
//ta

        adapter.onEditTask = { task ->

            val intent = Intent(activity, TaskActivity::class.java)
            intent.putExtra("Task",task)
            startActivityForResult(intent, ADD_TASK_REQUEST_CODE)

            adapter.notifyDataSetChanged()
        }



    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val task = data?.getSerializableExtra("Task") as Task
        if (resultCode == Activity.RESULT_OK ) {

            val index = adapter.currentList.indexOfFirst { it.id == task?.id }

            if(index == -1 ){
                viewModel.addTask(task)
            }else{

                    viewModel.editTask(task)



            }

            Log.d("size", adapter.currentList.size.toString())
            adapter.notifyDataSetChanged()


        }




    }


    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            val userInfo = Api.userService.getInfo().body()!!
            val apiText  =  view?.findViewById(R.id.apiTextV) as TextView
            apiText.text = "${userInfo.firstName} ${userInfo.lastName}"
        }

        viewModel.loadTasks()




    }





}
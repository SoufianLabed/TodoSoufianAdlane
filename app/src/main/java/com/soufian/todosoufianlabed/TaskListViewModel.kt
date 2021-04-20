package com.soufian.todosoufianlabed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soufian.todosoufianlabed.network.Api
import com.soufian.todosoufianlabed.network.TasksRepository
import com.soufian.todosoufianlabed.tasklist.Task
import kotlinx.coroutines.launch


class TaskListViewModel: ViewModel() {
    private val repository = TasksRepository()
    private val _taskList = MutableLiveData<List<Task>>()
    public val taskList: LiveData<List<Task>> = _taskList

      fun loadTasks() {
          viewModelScope.launch {
              val fetchedTasks = repository.loadTasks()
              _taskList.value = fetchedTasks!!
          }

    }

     fun deleteTask(task: Task) {

         viewModelScope.launch {
             val editableList = _taskList.value.orEmpty().toMutableList()
             val result = repository.deleteTask(task)
             if (result) {
                 editableList.remove(task)
                 _taskList.value = editableList
             }
         }


         }
    fun addTask(task: Task) {
        viewModelScope.launch {
            val editableList = _taskList.value.orEmpty().toMutableList()
            val new = repository.createTask(task)!!
            editableList.add(new)
            _taskList.value = editableList
        }
    }
    fun editTask(task: Task) {
        viewModelScope.launch {
            val updatedTask = repository.updateTask(task)!!
            val editableList = _taskList.value.orEmpty().toMutableList()
            val position = editableList.indexOfFirst { updatedTask.id == it.id }
            editableList[position] = updatedTask
            _taskList.value = editableList
        }
    }

}


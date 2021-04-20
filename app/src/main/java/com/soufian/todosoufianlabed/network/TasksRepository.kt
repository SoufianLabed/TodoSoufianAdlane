package com.soufian.todosoufianlabed.network

import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.soufian.todosoufianlabed.R
import com.soufian.todosoufianlabed.network.Api.taskwebservice
import com.soufian.todosoufianlabed.tasklist.Task
/*
class TasksRepository {
    private val tasksWebService = Api.taskwebservice

    // Ces deux variables encapsulent la même donnée:
    // [_taskList] est modifiable mais privée donc inaccessible à l'extérieur de cette classe
    private val _taskList = MutableLiveData<List<Task>>()
    // [taskList] est publique mais non-modifiable:
    // On pourra seulement l'observer (s'y abonner) depuis d'autres classes
    public val taskList: LiveData<List<Task>> = _taskList

    suspend fun refresh() {
        // Call HTTP (opération longue):
        val tasksResponse = tasksWebService.getTasks()
        // À la ligne suivante, on a reçu la réponse de l'API:
        if (tasksResponse.isSuccessful) {
            val fetchedTasks = tasksResponse.body()
            // on modifie la valeur encapsulée, ce qui va notifier ses Observers et donc déclencher leur callback
            _taskList.value = fetchedTasks!!
        }
    }

    suspend fun updateTask(task: Task) {
        // TODO: do update request and check response
        // ...
        val updatedTask =  tasksWebService.updateTask(task).body()!!
        val editableList = _taskList.value.orEmpty().toMutableList()
        val position = editableList.indexOfFirst { updatedTask.id == it.id }
        editableList[position] = updatedTask
        _taskList.value = editableList
    }

    suspend fun deleteTask(task: Task) {
        // TODO: do update request and check response
        // ...
        tasksWebService.deleteTask(task.id)
        val editableList = _taskList.value.orEmpty().toMutableList()
        editableList.remove(task)
        _taskList.value = editableList

    }

    suspend fun createTask(task: Task) {
        // TODO: do update request and check response
        // ...
        tasksWebService.createTask(task)
        val editableList = _taskList.value.orEmpty().toMutableList()
        editableList.add(task)
        _taskList.value = editableList

    }
}*/


class TasksRepository {
    // Le web service requête le serveur
    private val tasksWebService = Api.taskwebservice

    suspend fun loadTasks(): List<Task>? {
        val response = tasksWebService.getTasks()
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun deleteTask(task: Task): Boolean {
        // TODO: do update request and check response
        // ...
        val response = tasksWebService.deleteTask(task.id)
        return response.isSuccessful

    }

    suspend fun createTask(task: Task): Task? {
        val response = tasksWebService.createTask(task)
        return if (response.isSuccessful) response.body() else null
    }
    suspend fun updateTask(task: Task): Task? {
        val response = tasksWebService.updateTask(task)
        return if (response.isSuccessful) response.body() else null
    }
}
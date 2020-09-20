package com.tenisme.room_exam_kotlin.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.tenisme.room_exam_kotlin.db.AppDatabase
import com.tenisme.room_exam_kotlin.db.entity.Todo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {

    val todos: LiveData<List<Todo>> by lazy { getAll() }

    var newTodo: String? = null

    private val db = Room.databaseBuilder(application, AppDatabase::class.java, "todo-db")
        .build()

    fun getAll(): LiveData<List<Todo>> {
        return db.todoDao().getAll()
    }

    fun insert(todo: String) {
        viewModelScope.launch(Dispatchers.IO) { db.todoDao().insert(Todo(todo)) }
    }
}
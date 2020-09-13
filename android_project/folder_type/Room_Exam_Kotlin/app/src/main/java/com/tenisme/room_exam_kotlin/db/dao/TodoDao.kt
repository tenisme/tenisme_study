package com.tenisme.room_exam_kotlin.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.tenisme.room_exam_kotlin.db.entity.Todo

@Dao
interface TodoDao {
    @Query("SELECT * FROM Todo")
    fun getAll(): LiveData<List<Todo>>

    @Insert
    suspend fun insert(todo: Todo)

    @Update
    suspend fun update(todo: Todo)

    @Delete
    suspend fun delete(todo: Todo)
}
package com.tenisme.room_exam_kotlin.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tenisme.room_exam_kotlin.db.dao.TodoDao
import com.tenisme.room_exam_kotlin.db.entity.Todo

@Database(entities = [Todo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}
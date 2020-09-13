package com.tenisme.room_exam_kotlin.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Todo(@PrimaryKey(autoGenerate = true) val id: Int?, var title: String?) {
    constructor(title: String): this(null, title)
}
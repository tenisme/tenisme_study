package com.tenisme.mvvm_aac.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// MVVM 순서 0 : Gradle 에 사용할 라이브러리 등록
// MVVM 순서 1 : Entity 작성
@Entity
data class Contact(
    @PrimaryKey(autoGenerate = true)
    var id: Long?,
    var name: String,
    var number: String,
    var initial: Char
) {
    constructor() : this(null, "", "", '\u0000')
}
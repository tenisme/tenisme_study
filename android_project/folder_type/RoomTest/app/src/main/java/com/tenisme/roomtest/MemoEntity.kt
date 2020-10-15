package com.tenisme.roomtest

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memo")
data class MemoEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "memo_id")
    var memoId: Long?,

    var memo: String,
) {
    constructor(memo: String): this(null, memo)
}
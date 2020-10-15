package com.tenisme.roomtest

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MemoDao {

    @Insert
    fun insertMemo(entity: MemoEntity): Long

    @Query("SELECT * FROM memo")
    fun getAllMemoLive(): LiveData<List<MemoEntity>>

    @Query("SELECT * FROM memo WHERE memo_id = :id")
    fun getMemo(id: Long): MemoEntity
}
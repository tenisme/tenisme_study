package com.tenisme.roomtest

import android.app.Application
import androidx.lifecycle.LiveData

class MemoRepository(application: Application) {

    private val memoDatabase = MemoDatabase.getInstance(application)!!
    private val memoDao = memoDatabase.memoDao()

    fun insert(entity: MemoEntity): Long {
        return memoDao.insertMemo(entity)
    }

    fun getAllLive(): LiveData<List<MemoEntity>> {
        return memoDao.getAllMemoLive()
    }

    fun getMemo(id: Long): MemoEntity {
        return memoDao.getMemo(id)
    }

}
package com.tenisme.roomtest

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MemoViewModel(application: Application) : AndroidViewModel(application) {

    private val memoRepository = MemoRepository(application)

    suspend fun insert(entity: MemoEntity): Long {
        return withContext(viewModelScope.coroutineContext + Dispatchers.IO) {
            memoRepository.insert(entity)
        }
    }

    fun getAllMemoLive(): LiveData<List<MemoEntity>> {
        return memoRepository.getAllLive()
    }

    fun getMemo(id: Long): MemoEntity {
        return memoRepository.getMemo(id)
    }
}
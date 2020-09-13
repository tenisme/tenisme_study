package com.tenisme.mvvm_aac.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.tenisme.mvvm_aac.db.entity.Contact

// MVVM 순서 2 : DAO 작성
@Dao // DAO == Data Access Objects
interface ContactDao {

    @Query("SELECT * FROM Contact ORDER BY name ASC")
    fun getAll(): LiveData<List<Contact>>

    @Insert(onConflict = OnConflictStrategy.REPLACE) // 중복 데이터의 경우 기존 값을 대체
    fun insert(contact: Contact)

    @Delete
    fun delete(contact: Contact)

}
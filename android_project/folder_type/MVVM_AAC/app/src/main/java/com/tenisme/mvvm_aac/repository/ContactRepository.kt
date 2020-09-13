package com.tenisme.mvvm_aac.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.tenisme.mvvm_aac.db.ContactDatabase
import com.tenisme.mvvm_aac.db.dao.ContactDao
import com.tenisme.mvvm_aac.db.entity.Contact
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// MVVM 순서 4 : Repository 작성
// 직접적으로 DB에 접근할 수 있는 곳
class ContactRepository(application: Application) {

    private val contactDatabase = ContactDatabase.getInstance(application)!! // context 를 application 으로 하는 DB 호출(생성)
    private val contactDao: ContactDao = contactDatabase.contactDao() // contactDatabase 에 등록한 DAO 하나 호출
    private val contacts: LiveData<List<Contact>> = contactDao.getAll() // DAO 에 정의한 기능 중 하나(모든 contact list(LiveData) 가져오기)를 호출.

    // 이 아래에 DAO 에 있는 기능들을 전부 불러내고,
    // ViewModel 에서 'DB에 접근을 요청'할 때 수행할 함수를 만들어둔다

    fun getAll(): LiveData<List<Contact>> {
        return contacts
    }

    fun insert(contact: Contact) {
        contactDao.insert(contact)
    }

    fun delete(contact: Contact) {
        contactDao.delete(contact)
    }

}
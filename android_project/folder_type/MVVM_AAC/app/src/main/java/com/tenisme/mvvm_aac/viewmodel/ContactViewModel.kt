package com.tenisme.mvvm_aac.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.tenisme.mvvm_aac.db.entity.Contact
import com.tenisme.mvvm_aac.repository.ContactRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// MVVM 순서 5 : ViewModel 작성
// AndroidViewModel 에서는 Application 을 파라미터로 사용한다.
//      ViewModel 이 액티비티의 context 를 쓰게 되면, 액티비티가 destroy 된 경우에는 메모리 릭이 발생할 수 있다.
//      따라서 Application Context 를 사용하기 위해 Application 을 인자로 받는다
class ContactViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ContactRepository(application) // ContactRepository 호출
    private val contacts = repository.getAll() // repository 의 .getAll() 가져오기

    // DB를 제어할 함수는 Repository 에 있는 함수를 이용해 설정
    // Room DB를 메인 스레드에서 접근하려 하면 크래쉬가 발생하므로 별도의 스레드에서 접근해야 함 주의.

    fun getAll(): LiveData<List<Contact>> {
        return this.contacts
    }

    fun insert(contact: Contact) {
        viewModelScope.launch(Dispatchers.IO) { repository.insert(contact) }
    }

    fun delete(contact: Contact) {
        viewModelScope.launch(Dispatchers.IO) { repository.delete(contact) }
    }

}
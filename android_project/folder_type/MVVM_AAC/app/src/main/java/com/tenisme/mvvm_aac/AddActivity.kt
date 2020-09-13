package com.tenisme.mvvm_aac

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.tenisme.mvvm_aac.databinding.ActivityAddBinding
import com.tenisme.mvvm_aac.databinding.ActivityMainBinding
import com.tenisme.mvvm_aac.db.entity.Contact
import com.tenisme.mvvm_aac.viewmodel.ContactViewModel
import kotlinx.android.synthetic.main.activity_add.*

// MVVM 순서 11 : activity_add.xml 작성
// MVVM 순서 12 : AddActivity.kt 작성
class AddActivity : AppCompatActivity() {

    private val contactViewModel by lazy {
        ViewModelProvider(this).get(ContactViewModel::class.java)
    } // 2) ViewModel 객체를 만든다.

    private var id: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding =
            DataBindingUtil.setContentView<ActivityAddBinding>(this, R.layout.activity_add)

        binding.lifecycleOwner = this
        binding.contactViewModel = this.contactViewModel

        // intent null check & get extras
        if (intent != null && intent.hasExtra(EXTRA_CONTACT_NAME) && intent.hasExtra(EXTRA_CONTACT_NUMBER)
            && intent.hasExtra(EXTRA_CONTACT_ID)) {
            // 3) 만약 intent 가 null 이 아니고, extra 에 주소록 정보가 모두 들어있다면 EditText 와 id 값을 지정해준다
            // MainActivity 에서 ADD 버튼을 눌렀을 때에는 신규 추가이므로 인텐트가 없고,
            // RecyclerView item 을 눌러 편집을 할 때에는 해당하는 정보를 불러오기 위해 인텐트 값을 불러올 것이다.
            binding.editAddName.setText(intent.getStringExtra(EXTRA_CONTACT_NAME))
            binding.editAddNumber.setText(intent.getStringExtra(EXTRA_CONTACT_NUMBER))
            id = intent.getLongExtra(EXTRA_CONTACT_ID, -1)
        }

        // 4) DONE 버튼을 통해 EditText 의 null 체크를 한 후,
        // ViewModel 을 통해 insert 해주고, MainActivity 로 돌아간다.
        binding.btnAdd.setOnClickListener {
            val name = binding.editAddName.text.toString().trim()
            val number = binding.editAddNumber.text.toString()

            if (name.isEmpty() || number.isEmpty()) {
                Toast.makeText(this, "Please enter name and number.", Toast.LENGTH_SHORT).show()
            } else {
                val initial = name[0].toUpperCase()
                val contact = Contact(id, name, number, initial)
                // 아이디 값이 null 일 경우 Room 에서 자동으로 id를 생성해주면서 새로운 contact 를 DB에 추가한다.
                // id 값을 Main 에서 intent 로 받아온 경우, 완료 버튼을 누르면 해당 아이템을 수정하게 된다.(OnConflictStrategy.REPLACE)
                contactViewModel.insert(contact)
                finish()
            }
        }
    }

    // 1) intent extra 로 사용할 상수를 만든다. (companion object)
    companion object {
        const val EXTRA_CONTACT_NAME = "EXTRA_CONTACT_NAME"
        const val EXTRA_CONTACT_NUMBER = "EXTRA_CONTACT_NUMBER"
        const val EXTRA_CONTACT_ID = "EXTRA_CONTACT_ID"
    }
}
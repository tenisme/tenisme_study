package com.tenisme.mvvm_aac

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tenisme.mvvm_aac.adapter.ContactAdapter
import com.tenisme.mvvm_aac.databinding.ActivityMainBinding
import com.tenisme.mvvm_aac.db.entity.Contact
import com.tenisme.mvvm_aac.viewmodel.ContactViewModel
import kotlinx.android.synthetic.main.activity_main.*

// MVVM 순서 6 : MainActivity 설정
// 우선적으로 MainActivity 에서 해줄 일은 ContactViewModel 인스턴스를 만들고, 이를 관찰하는 역할이다.
// MVVM 순서 10 : Observer 의 onChanged 에 해당하는 식에는 Adapter 를 통해 UI를 업데이트 하도록 지정
// MVVM 순서 13 : MainActivity 와 AddActivity 연결
class MainActivity : AppCompatActivity() {

    private val contactViewModel by lazy {
        ViewModelProvider(this).get(ContactViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        binding.lifecycleOwner = this
        binding.contactViewModel = this.contactViewModel

        // Set contactItemClick & contactItemLongClick lambda
        // ★ Adapter 에 onClick 시에 해야할 일과 onLongClick 시에 해야할 일 두 개의 (Contact) -> Unit 파라미터를 넘겨준다.
        // 클릭했을 때에는 현재 contact 에서 name, number, id를 뽑아 인텐트에 포함시켜 AddActivity 로 넘겨주면서
        // 액티비티를 시작하도록 만들 것이다. AddActivity 를 우선 만든 후에 수정해 주기로 한다.
        // 롱클릭 했을 때에는 다이얼로그를 통해 아이템을 삭제하도록 만들었다.
        val adapter = ContactAdapter({ contact ->
            // 해당 아이템을 수정하기 위해 intent 를 통해 contact 정보를 extra 로 추가하고 AddActivity 시작
            val intent = Intent(this, AddActivity::class.java)
            intent.putExtra(AddActivity.EXTRA_CONTACT_NAME, contact.name)
            intent.putExtra(AddActivity.EXTRA_CONTACT_NUMBER, contact.number)
            intent.putExtra(AddActivity.EXTRA_CONTACT_ID, contact.id)
            startActivity(intent)
        }, { contact ->
            deleteDialog(contact)
        })

        binding.mainRecyclerView.adapter = adapter
        binding.mainRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.mainRecyclerView.setHasFixedSize(true)

        // Observer 를 만들어서 뷰모델이 어느 액티비티/프래그먼트의 생명주기를 관찰할 것인지 정한다
        // 이 액티비티가 파괴되면 시점에 시스템에서 뷰모델도 자동으로 파괴할 것이다.
        // 뷰에서는 Observer.onChanged()를 통해 이를 관찰하고 있으므로 자동으로 UI를 갱신한다.
        contactViewModel.getAll().observe(this, { contacts ->
            adapter.setContacts(contacts!!)
        })

        // ADD 버튼을 눌렀을 때에 새로운 주소록 추가를 위해 AddActivity 를 시작
        binding.mainButton.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }
    }

    private fun deleteDialog(contact: Contact) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Delete selected contact?")
            .setNegativeButton("NO") { _, _ -> }
            .setPositiveButton("YES") { _, _ ->
                contactViewModel.delete(contact)
            }
        builder.show()
    }
}
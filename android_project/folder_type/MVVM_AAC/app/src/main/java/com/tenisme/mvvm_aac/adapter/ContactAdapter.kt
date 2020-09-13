package com.tenisme.mvvm_aac.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tenisme.mvvm_aac.R
import com.tenisme.mvvm_aac.databinding.ItemContactBinding
import com.tenisme.mvvm_aac.db.entity.Contact

// MVVM 순서 7 : item_contact.xml 작성
// MVVM 순서 8 : activity_name.xml 작성
// MVVM 순서 9 : ContactAdapter.kt(리사이클러뷰 어댑터) 작성
// ContactAdapter({ contactItemClick }, { contactItemLongClick }) 형태로,
// 클릭했을 때의 액션과 롱클릭 했을 때의 액션을 각각 MainActivity 에서 넘겨주는 방식을 사용했다
class ContactAdapter(val contactItemClick: (Contact) -> Unit, val contactItemLongClick: (Contact) -> Unit)
    : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {
    private var contacts: List<Contact> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // 데이터 바인딩을 사용하면 뷰 홀더에 View 를 보내주는 것이 아닌 바인딩 클래스를 보내줘야 하므로
        // 코드도 그에 맞춰 바뀌게 된다.
        val binding = ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.onBind(contacts[position])
    }

    // View 타입을 받는 것이 아니라 바인딩된 클래스를 받음
    inner class ViewHolder(private val binding: ItemContactBinding): RecyclerView.ViewHolder(binding.root) {
//        private val nameTv = itemView.findViewById<TextView>(R.id.item_tv_name)
//        private val numberTv = itemView.findViewById<TextView>(R.id.item_tv_number)
//        private val initialTv = itemView.findViewById<TextView>(R.id.item_tv_initial)

        // item_contact.xml 의 contact 변수에 어댑터를 통해 전달돼 온 Contact 를 넣어준다.
        fun onBind(contact: Contact) {
            binding.contact = contact
//            nameTv.text = contact.name
//            numberTv.text = contact.number
//            initialTv.text = contact.initial.toString()

            itemView.setOnClickListener {
                contactItemClick(contact)
            }

            itemView.setOnLongClickListener {
                contactItemLongClick(contact)
                true
            }
        }
    }

    // View 에서 화면을 갱신할 때 사용할 함수
    // 데이터베이스가 변경될 때마다 이 함수를 호출함
    fun setContacts(contacts: List<Contact>) {
        this.contacts = contacts
        notifyDataSetChanged()
    }

}
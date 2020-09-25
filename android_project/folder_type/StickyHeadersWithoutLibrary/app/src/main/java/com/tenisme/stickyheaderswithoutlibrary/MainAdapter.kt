package com.tenisme.stickyheaderswithoutlibrary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*

// Adapter 엔 Data 를 모델로 한 리스트를 매개 변수로 넣어준다.
class MainAdapter(private var item: List<Data>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val recyclerItemList: ArrayList<AdapterItem> = ArrayList()

    init {
        setListData()
    }

    // 최상단에 올라갈 뷰를 TYPE_TOP 으로 정한다.
    // 그 다음 리스트에서 고정될 뷰는 TYPE_HOLDER
    // 리스트가 그려질 뷰는 TYPE_LIST
    // 리스트가 없으면 TYPE_EMPTY
    companion object {
        const val TYPE_TOP = 0
        const val TYPE_HOLDER = 1
        const val TYPE_EMPTY = 2
        const val TYPE_LIST = 3
    }

    // 여기서 ViewType 이 TYPE_HOLDER 일 경우에 스크롤 시 상단에 그려진다는 것을 유의하자
    // TYPE_TOP 과 혼돈 주의
    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TYPE_TOP
            1 -> TYPE_HOLDER
            2 -> TYPE_EMPTY
            else -> TYPE_LIST
        }
    }

    // 각 뷰에 해당하는 layout xml 을 만들고 "뷰홀더에 bind" 해준다.
    // Adapter 내에서 각 View Type 에 맞게 뷰 홀더를 만들어 바인드 해준다.
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerView.ViewHolder {
        val view: View
        return when (recyclerItemList[position].type) {
            TYPE_TOP -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.top_item, parent, false)
                TopViewHolder(view)
            }
            TYPE_HOLDER -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.hold_item, parent, false)
                HolderViewHolder(view)
            }
            TYPE_EMPTY -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.empty_item, parent, false)
                EmptyViewHolder(view)
            }
            else -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item, parent, false)
                ItemViewHolder(view)
            }
        }
    }

    fun isHolder(position: Int): Boolean {
        return recyclerItemList[position].type == TYPE_HOLDER
    }

    // MainActivity 에서 넘어옴 -> 다시 ItemDecoration 로
    fun getHeaderLayoutView(list: RecyclerView, position: Int): View? {
        val lastIndex =
            if (position < recyclerItemList.size) position else recyclerItemList.size - 1
        for (index in lastIndex downTo 0) {
            val model = recyclerItemList[index]
            if (model.type == TYPE_HOLDER) {
                return LayoutInflater.from(list.context)
                    .inflate(R.layout.hold_item, list, false);
            }
        }
        return null
    }

    // setListData() 메소드를 만들어서 이 리스트에 아이템을 add 할 건데 다음과 같이 해준다.
    // 이렇게 하면 recyclerItemList 에는
    //      Index 0 : TYPE_TOP, DATA("",-1)
    //      Index 1  : TYPE_HOLDER, DATA("",-1)
    //      Index 2 : TYPE_LIST, DATA("",-1)
    // 총 3개의 아이템이 들어가 있다.
    // (TYPE_EMPTY 의 경우는 item 이 없을 때 TYPE_LIST 대신 들어간다)
    private fun setListData() {
        recyclerItemList.clear()
        recyclerItemList.add(AdapterItem(TYPE_TOP, Data("", -1)))
        recyclerItemList.add(AdapterItem(TYPE_HOLDER, Data("", -1)))
        if (item.isEmpty()) {
            recyclerItemList.add(AdapterItem(TYPE_EMPTY, Data("", -1)))
        } else {
            for (data in item) {
                recyclerItemList.add(AdapterItem(TYPE_LIST, data))
            }
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = recyclerItemList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TopViewHolder -> {
                holder.bindView()
            }
            is HolderViewHolder -> {
                holder.bindView()
            }
            is EmptyViewHolder -> {
                holder.bindView()
            }
            is ItemViewHolder -> {
                val item: Data = recyclerItemList[position].objects
                holder.bindView(item)
            }
        }
    }

    inner class TopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView() {
        }
    }

    inner class HolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView() {
        }
    }

    inner class EmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView() {
        }
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(item: Data) {
            itemView.tv_name.text = item.name
            itemView.tv_age.text = item.age.toString()
        }
    }
}
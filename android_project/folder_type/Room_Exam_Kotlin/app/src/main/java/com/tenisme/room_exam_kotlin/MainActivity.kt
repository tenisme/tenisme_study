package com.tenisme.room_exam_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tenisme.room_exam_kotlin.databinding.ActivityMainBinding
import com.tenisme.room_exam_kotlin.db.entity.Todo
import com.tenisme.room_exam_kotlin.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ViewModel + DataBinding 사용 예시
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.lifecycleOwner = this // dataBinding 에서 LiveData 를 활용하기 위한 셋팅

        val viewModel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory(application))
            .get(MainViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.getAll().observe(this, { binding.editTodo.text = null })

        // 뷰모델 사용 + DataBinding 이 없을 때의 사용 예시
//        viewModel.getAll().observe(this, { txtResult.text = it.toString() })
//
//        btnAdd.setOnClickListener {
//            // 액티비티에서 코루틴 사용하기
//            lifecycleScope.launch(Dispatchers.IO) {
//                // 여기 내부의 코드는 백그라운드에서 돌아간다.
//                viewModel.insert(Todo(editTodo.text.toString())) // 비동기 처리를 해야하는 부분
//            }
//        }

        // 뷰모델이 없을 때 + DataBinding 이 없을 때의 사용 예시
//        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "todo-db")
//            .build()

//        db.todoDao().getAll().observe(this, {
//            txtResult.text = it.toString()
//        })
//
//        btnAdd.setOnClickListener {
//            // 액티비티에서 코루틴 사용하기
//            lifecycleScope.launch(Dispatchers.IO) {
//                // 여기 내부의 코드는 백그라운드에서 돌아간다.
//                db.todoDao().insert(Todo(editTodo.text.toString())) // 비동기 처리를 해야하는 부분
//            }
//        }
    }
}
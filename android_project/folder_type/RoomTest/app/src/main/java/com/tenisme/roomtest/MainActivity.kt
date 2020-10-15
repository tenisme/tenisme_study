package com.tenisme.roomtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.tenisme.roomtest.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import java.lang.Runnable

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    private val memoViewModel by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application))
            .get(MemoViewModel::class.java)
    }

    private var sentenceId: Long? = null
    private var sentences: List<MemoEntity>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding.lifecycleOwner = this
        binding.memoViewModel = memoViewModel

        memoViewModel.getAllMemoLive().observe(this, {
            Log.d("Test", "Observing Sentences : $it")
        })

        runBlocking {
            GlobalScope.launch(Dispatchers.IO) {
                sentenceId = memoViewModel.insert(MemoEntity("Test2"))
                Log.d("Test", "InsertedSentenceId : $sentenceId")

                val sentence = memoViewModel.getMemo(sentenceId!!)
                Log.d("Test", "Sentence in Scope : $sentence")
            }
        }
    }
}
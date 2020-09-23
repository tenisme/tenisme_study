package com.tenisme.firestore

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = FirebaseFirestore.getInstance() // 파이어베이스의 인스턴스를 가져옴
        // 메모리에 파이어베이스를 사용할 수 있는 객체가 생성됐다. 클래스를 인스턴스화했다.
        // 클래스가 메모리로 올라온 게 인스턴스다.
        // 객체를 만들어줘야 사용할 수 있음.

        // 데이터 인서트하기
        // Create a new user with a first and last name
        val user: MutableMap<String, Any> = HashMap()
        // Map 을 사용하는 이유 : 파이어스토어가 키 밸류로 이루어져있기 때문임.
        user["first"] = "Ada" // "키 = 밸류"
        user["last"] = "Lovelace"
        user["born"] = 1815
        // 위 데이터를 파이어스토어에 넣을 거임.
        // 파이어스토어에서는 위 Map 이 도큐멘트가 됨.

        // "Add a new document" with a generated ID
        // 파이어스토어에 위에서 만든 Map 추가하기
        db.collection("users") // "users" 테이블이라고 생각하면 됨.
                .add(user)
                .addOnSuccessListener { documentReference ->
                    // 제대로 저장이 되면 이 함수가 호출됨
                    Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.id) }
                .addOnFailureListener { e ->
                    // 실패하면 이 함수가 호출됨
                     Log.w("TAG", "Error adding document", e) }

        // 데이터 불러오기
        db.collection("users")
                .get()
                .addOnCompleteListener { task -> // 가져왔을 때의 콜백함수
                    if (task.isSuccessful) {
                        for (document in task.result!!) {
                            Log.d("TAG", document.id + " => " + document.data)
                        }
                    } else {
                        Log.w("TAG", "Error getting documents.", task.exception)
                    }
                }
    }
}
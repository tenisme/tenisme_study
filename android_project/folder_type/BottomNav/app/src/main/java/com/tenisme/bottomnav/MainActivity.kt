package com.tenisme.bottomnav

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private val bottomNavView by lazy { findViewById<BottomNavigationView>(R.id.bottomNavView) }

    // 이렇게 프래그먼트를 액티비티의 멤버변수로 만들어놓으면 이 액티비티가 꺼질 때까지 이 프래그먼트의 데이터들은 보존된다.
    private val firstFragment by lazy { FirstFragment() }
    private val secondFragment by lazy { SecondFragment() }
    private val thirdFragment by lazy { ThirdFragment() }

    // 프래그먼트는 액티비티 안에서 관리한다.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadFragment(firstFragment)
        supportActionBar!!.title = "Home"

        bottomNavView.setOnNavigationItemSelectedListener { menuItem ->
            var fragment: Fragment? = null
            when (menuItem.itemId) {
                R.id.firstFragment -> {
                    fragment = firstFragment
                    supportActionBar!!.title = "Home"
                }
                R.id.secondFragment -> {
                    fragment = secondFragment
                    supportActionBar!!.title = "오디오 리스트"
                }
                R.id.thirdFragment -> {
                    fragment = thirdFragment
                    supportActionBar!!.title = "내정보"
                }
            }
            // 첫번째 프래그먼트부터 시작할 것이므로 함수에 firstFragment 를 보냄
            loadFragment(fragment)
        }

        // 탭 바꿔질 때마다 새 메모리에 올려지는 방식
        // 새롭게 계속 실행되는(메모리에 올라가는) 것임.
        // 처음부터 시작할 수밖에 없음. 상태 기억을 못함.
//        // BottomNavigationView 연결
//        bottomNavView = findViewById(R.id.bottomNavView)
//        // 화면이 바뀌려면 컨트롤러가 필요함. 그래서 컨트롤러 만듦.
//        val navController = Navigation.findNavController(this, R.id.fragment)
//
//        // 액션바의 타이틀을 프래그먼트에 맞게 변경하고 싶을 때
//        val appBarConfiguration = AppBarConfiguration.Builder(
//            R.id.firstFragment, R.id.secondFragment, R.id.thirdFragment
//        ).build()
//        // AppBarConfiguration 을 내비게이션 UI에 연결
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
//
//        // 뷰와(탭과) 컨트롤러를 결합.
//        NavigationUI.setupWithNavController(bottomNavView, navController)

        // 실무에서 쓰이는 방식

    }

    private fun loadFragment(fragment: Fragment?): Boolean {
        if (fragment != null) {
            // 이미 한 번 만들어놓은 프래그먼트를 유지시켜줌
            supportFragmentManager.beginTransaction().replace(R.id.fragment, fragment).commit()
            return true
        }
        return false
    }
}
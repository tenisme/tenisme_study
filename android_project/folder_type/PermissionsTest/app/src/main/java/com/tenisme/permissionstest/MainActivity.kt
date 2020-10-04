package com.tenisme.permissionstest

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

// 이 페이지는 최종 테스트를 해보지 않음
// 새로 만든(잘 돌아가는 거 확인한) 테스트 페이지는 MainActivity2
class MainActivity : AppCompatActivity() {

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var switch1: Switch
    private lateinit var layoutMain: ConstraintLayout
    private val sp by lazy { getSharedPreferences("permissionTest", MODE_PRIVATE) }
    private val edit by lazy { sp.edit() }
    private var isFirstCheckOfPermission = false
    private var isGrantedPermission = false
    private var isLockScreenOn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        layoutMain = findViewById(R.id.layoutMain)
        switch1 = findViewById(R.id.switch1)

        if(sp.getBoolean("runAppFirst", true)) {
            edit.putBoolean("isFirstCheckOfPermission", true)
            edit.putBoolean("isGrantedPermission", false)
            edit.putBoolean("isLockScreenOn", false)
            edit.putBoolean("runAppFirst", false)
            edit.apply()
        }

        isLockScreenOn = sp.getBoolean("isLockScreenOn", false)
        isGrantedPermission = sp.getBoolean("isGrantedPermission", false)
        switch1.isChecked = isLockScreenOn and isGrantedPermission

        switch1.setOnCheckedChangeListener { _, b ->
            when(b) {
                true -> {
                    // todo : 권한이 두개 다 설정된 경우에는 이 다이얼로그를 띄우지 않고 바로 온/오프가 되도록 코딩

                    isFirstCheckOfPermission = sp.getBoolean("isFirstCheckOfPermission", true)

                    if(isFirstCheckOfPermission) {
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("권한 설정 알림")
                        builder.setMessage("\n잠금화면에서 시간 기록 기능을 정상적으로 사용하기 위해서는 아래의 권한 및 설정이 필요합니다." +
                                "\n\n전화 상태 읽기 권한 : 전화가 올 때는 시간 기록 잠금화면을 표시하지 않도록 '전화 상태 알림'을 받기 위해 필요합니다." +
                                "\n\n다른 앱 위에 화면 표시 설정 : 기기 잠금 상태에서 시간 기록 화면을 띄우기 위해 필요합니다.")
                            .setNegativeButton("취소") { _, _ ->
                                switch1.isChecked = false
                            }
                            .setPositiveButton("권한 설정") { _, _ ->
                                checkAndRequestPermission()
                            }
                        builder.setCancelable(false)
                        builder.show()
                    } else {
                        if(!isGrantedPermission) {
                            checkAndRequestPermission()
                        } else {
                            edit.putBoolean("isLockScreenOn", true)
                            edit.apply()
                            isLockScreenOn = sp.getBoolean("isLockScreenOn", false)
                            if(isLockScreenOn) switch1.isChecked = isLockScreenOn
                            Toast.makeText(this, "잠금화면 기능 실행", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                false -> {
                    edit.putBoolean("isLockScreenOn", false)
                    edit.apply()
                    isLockScreenOn = sp.getBoolean("isLockScreenOn", false)
                    if(isLockScreenOn) switch1.isChecked = isLockScreenOn
                    Toast.makeText(this, "잠금화면 기능 종료", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun checkAndRequestPermission(): Boolean {

        lateinit var permissions: MutableList<String>
        // 마시멜로우 이상일 경우 권한 체크
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissions = mutableListOf(
                Manifest.permission.READ_PHONE_STATE, // 전화 상태(통화 상태)에 대한 읽기 전용 액세스 권한
            )
        } else {
            Toast.makeText(this, "모든 권한 얻기 성공", Toast.LENGTH_SHORT).show()
            return true
        }

        val arrayPermission = permissions.toTypedArray()

        if (hasAllPermissions(this, arrayPermission)) {
            Toast.makeText(this, "모든 권한 얻기 성공", Toast.LENGTH_SHORT).show()
            return true
        } else {
            if(hasPermissionsDenied(this, arrayPermission)) {
                val snackBar = Snackbar.make(
                    layoutMain,
                    "잠금화면 기능을 정상적으로 사용하기 위해서는 요청하는 모든 권한에 대한 승인이 필요합니다.",
                    Snackbar.LENGTH_INDEFINITE
                )
                snackBar.setAction("권한승인") {
                    ActivityCompat.requestPermissions(this, arrayPermission, PERMISSION_REQUEST_CODE)
                    getOverlayPermission()
                }
                snackBar.show()
            } else {
                if (isFirstCheckOfPermission) {
                    // 2.2. 승인요청을 한적이 없는 경우
                    // 권한 승인을 처음 물어봤는가에 대한 여부를 SP에 저장 후
                    edit.putBoolean("isFirstCheckOfPermission", false)
                    edit.apply()
                    isFirstCheckOfPermission = sp.getBoolean("isFirstCheckOfPermission", true)
                    // 권한을 (처음) 요청
                    ActivityCompat.requestPermissions(this, arrayPermission, PERMISSION_REQUEST_CODE)
                    getOverlayPermission()
                } else {
                    // 2.3. 사용자가 권한을 거부하면서 "다시 묻지않음" 옵션을 선택한 경우
                    // requestPermissions 를 요청해도 창이 나타나지 않기 때문에
                    // 유저가 직접 앱 설정에서 권한을 승인할 수 있도록 이동을 유도한다.
                    val snackBar = Snackbar.make(
                        layoutMain,
                        "잠금화면 기능을 정상적으로 사용하기 위해 필요한 권한이 있습니다. 확인을 누르면 설정 화면으로 이동합니다.",
                        Snackbar.LENGTH_INDEFINITE
                    )
                    snackBar.setAction("확인") {
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    }
                    snackBar.show()
                }
            }
            return false
        }
    }

    private fun hasAllPermissions(context: Context, permissions: Array<String>): Boolean =
            permissions.all {
                // .all {} <= 객체들을 내부 조건에 각각 하나씩 대응시키고, 그 결과가 전부 true 여야 true 를 반환함
                ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            }

    private fun hasPermissionsDenied(activity: Activity, permissions: Array<String>): Boolean =
            permissions.any {
                // .any {} <= 객체들을 내부 조건에 각각 하나씩 대응시키고, 하나라도 true 이면 true 를 반환.
                // 아래 조건에서 true 는 해당 권한에 대해 '승인 거절'을 했음을 의미함
                ActivityCompat.shouldShowRequestPermissionRationale(activity, it)
            }

    private fun getOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) { // 다른앱 위에 그리기 체크
                // 권한이 체크되어있지 않은 경우
                val uri: Uri = Uri.fromParts("package", packageName, null)
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, uri)
                startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST_CODE)
            } else {
                // 권한이 이미 체크되어있는 경우
                if (hasAllPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE))) {
                    edit.putBoolean("isGrantedPermission", true)
                    edit.apply()
                    switch1.isChecked = true
                    Toast.makeText(this, "모든 권한 얻기 성공", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == PERMISSION_REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(hasAllPermissions(this, permissions as Array<String>) and Settings.canDrawOverlays(this)) {
                    Toast.makeText(this, "모든 권한 얻기 성공", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == OVERLAY_PERMISSION_REQUEST_CODE) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                if (!Settings.canDrawOverlays(this)) {
//                    // 권한을 체크하지 않고 돌아온 경우
//                    finish()
//                } else {
//                    // 권한을 체크하고 돌아온 경우
//                    if (hasAllPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE))) {
//                        edit.putBoolean("isGrantedPermission", true)
//                        edit.apply()
//                        switch1.isChecked = true
//                        Toast.makeText(this, "모든 권한 얻기 성공", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        }
//    }

    companion object {
        const val PERMISSION_REQUEST_CODE = 0
        const val OVERLAY_PERMISSION_REQUEST_CODE = 1
    }
}
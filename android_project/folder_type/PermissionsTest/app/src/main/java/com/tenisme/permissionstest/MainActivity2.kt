package com.tenisme.permissionstest

import android.Manifest.permission.READ_PHONE_STATE
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.*
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

class MainActivity2 : AppCompatActivity() {

    private lateinit var toggleButton: ToggleButton
    private lateinit var layoutMain: ConstraintLayout
    private val sp by lazy { getSharedPreferences("permissionTest", MODE_PRIVATE) }
    private val edit by lazy { sp.edit() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        layoutMain = findViewById(R.id.layoutMain)
        toggleButton = findViewById(R.id.toggleButton)

        if (sp.getBoolean("runAppFirst", true)) {
            edit.putBoolean("checkPermissionsFirst", true)
            edit.putBoolean("runAppFirst", false)
            edit.apply()
        }

        val isLockScreenOn = sp.getBoolean("isLockScreenOn", false)
        Log.d("PermissionsTest", "isLockScreenOn : $isLockScreenOn")
        toggleButton.isChecked = isLockScreenOn

        toggleButton.setOnCheckedChangeListener { _, b ->
            when (b) {
                true -> {
                    if (!checkPermission(this, arrayOf(READ_PHONE_STATE))) {
                        // 권한이 승인되지 않은 상태
                        if (checkDeniedPermission(this, arrayOf(READ_PHONE_STATE))) {
                            // 권한 승인 요청을 거절한 경우
                            val snackBar = Snackbar.make(
                                layoutMain,
                                "이 기능을 정상적으로 사용하기 위해서는 요청하는 모든 권한에 대한 승인이 필요합니다.",
                                Snackbar.LENGTH_INDEFINITE
                            )
                            snackBar.setAction("권한 설정") {
                                ActivityCompat.requestPermissions(
                                    this, arrayOf(READ_PHONE_STATE), PERMISSION_REQUEST_CODE
                                )
                            }
                            snackBar.show()
                        } else {
                            if (sp.getBoolean("checkPermissionsFirst", true)) {
                                // 처음으로 권한 승인을 요청하는 경우
                                val builder = AlertDialog.Builder(this)
                                builder.setTitle("권한 설정 알림")
                                builder.setMessage("\n'잠금화면에서 시간 기록' 기능을 정상적으로 사용하기 위해서는 아래의 권한 및 설정이 필요합니다." +
                                        "\n\n · 전화 상태 읽기 권한 :\n전화가 올 때는 시간 기록 잠금화면을 표시하지 않도록 '전화 상태 알림'을 받기 위해 필요합니다." +
                                        "\n\n · 다른 앱 위에 화면 표시 설정 :\n기기 잠금 상태에서 시간 기록 화면을 띄우기 위해 필요합니다.")
                                    .setNegativeButton("취소") { _, _ ->
                                        toggleButton.isChecked = false
                                    }
                                    .setPositiveButton("권한 설정") { _, _ ->
                                        ActivityCompat.requestPermissions(
                                            this, arrayOf(READ_PHONE_STATE), PERMISSION_REQUEST_CODE
                                        )
                                        edit.putBoolean("checkPermissionsFirst", false)
                                        edit.apply()
                                    }
                                builder.setCancelable(false)
                                builder.show()
                            } else {
                                // 권한 승인 거절 + "다시 묻지 않음" 옵션을 선택한 경우
                                // requestPermissions() 로 권한을 요청해도 창이 나타나지 않기 때문에
                                // 유저가 직접 앱 설정에서 권한을 승인할 수 있도록 이동을 유도한다.
                                toggleButton.isChecked = false

                                val snackBar = Snackbar.make(
                                    layoutMain,
                                    "잠금화면 기능을 정상적으로 사용하기 위해 승인이 필요한 권한들이 있습니다.",
                                    Snackbar.LENGTH_INDEFINITE
                                )
                                snackBar.setAction("권한 설정") {
                                    val uri = Uri.fromParts("package", packageName, null)
                                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri)
//                                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
//                                    intent.data = uri
                                    startActivity(intent)
                                }
                                snackBar.show()
                            }
                        }
                    } else {
                        // 통화 상태 읽기 권한이 승인된 상태
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            // 다른 앱 위에 화면 표시 설정 요청
                            if (!Settings.canDrawOverlays(this)) {
                                val uri: Uri = Uri.fromParts("package", packageName, null)
                                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, uri)
                                startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST_CODE)
                                return@setOnCheckedChangeListener
                            }
                        }
                        // 두가지 권한 모두 설정된 상태
                        edit.putBoolean("isLockScreenOn", true)
                        edit.apply()

                        Toast.makeText(this, "LockScreen On 1", Toast.LENGTH_SHORT).show()
                    }
                }
                false -> {
                    edit.putBoolean("isLockScreenOn", false)
                    edit.apply()

                    Toast.makeText(this, "LockScreen Off 1", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.canDrawOverlays(this)) {
                        val uri: Uri = Uri.fromParts("package", packageName, null)
                        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, uri)
                        startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST_CODE)
                    } else {
                        toggleButton.isChecked = true

                        edit.putBoolean("isLockScreenOn", true)
                        edit.apply()

                        Toast.makeText(this, "LockScreen On 2", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    toggleButton.isChecked = true

                    edit.putBoolean("isLockScreenOn", true)
                    edit.apply()

                    Toast.makeText(this, "LockScreen On 3", Toast.LENGTH_SHORT).show()
                }
            } else {
                toggleButton.isChecked = false

                edit.putBoolean("isLockScreenOn", false)
                edit.apply()

                Toast.makeText(this, "LockScreen Off 2", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == OVERLAY_PERMISSION_REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    val snackBar = Snackbar.make(
                        layoutMain,
                        "두번째 권한을 설정해주지 않아 이 기능을 사용할 수 없습니다.",
                        Snackbar.LENGTH_INDEFINITE
                    )
                    snackBar.setAction("권한 설정") {
                        val uri: Uri = Uri.fromParts("package", packageName, null)
                        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, uri)
                        startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST_CODE)
                    }
                    snackBar.show()

                    toggleButton.isChecked = false

                    edit.putBoolean("isLockScreenOn", false)
                    edit.apply()

                    Toast.makeText(this, "LockScreen Off 3", Toast.LENGTH_SHORT).show()
                } else {
                    toggleButton.isChecked = true

                    edit.putBoolean("isLockScreenOn", true)
                    edit.apply()

                    Toast.makeText(this, "LockScreen On 4", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun checkPermission(context: Context, permissions: Array<String>) =
        permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PERMISSION_GRANTED
        }

    private fun checkDeniedPermission(activity: Activity, permissions: Array<String>) =
        permissions.any {
            ActivityCompat.shouldShowRequestPermissionRationale(activity, it)
        }

    companion object {
        const val PERMISSION_REQUEST_CODE = 0
        const val OVERLAY_PERMISSION_REQUEST_CODE = 1
    }

}
package com.tenisme.lockscreen

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent

class ScreenReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent!!.action.equals(Intent.ACTION_SCREEN_OFF)) {
            val i = Intent(context, LockScreenActivity::class.java)
            val component = ComponentName(context!!, LockScreenActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            i.component = component
            context.startActivity(i)
        }
    }
}
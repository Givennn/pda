package com.panda.pda.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.panda.pda.library.android.NotificationUtil

class MainActivity : AppCompatActivity() {

    private val notificationUtil: NotificationUtil by lazy { NotificationUtil(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

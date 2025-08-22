package com.simplemessenger

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        findViewById<Button>(R.id.btnCall).setOnClickListener {
            startActivity(Intent(this, CallActivity::class.java))
        }
    }
}

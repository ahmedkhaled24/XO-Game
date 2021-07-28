package com.example.tictactoee

import android.content.Intent
import android.graphics.drawable.Icon
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()
    }

    fun onePlayerEvent(view: View) {
        var i = Intent( this,MainActivity::class.java)
        i.putExtra("state","1")
        startActivity(i)
    }

    fun twoPlayerEvent(view: View) {
            var i = Intent(this,LoginActivity::class.java)
            i.putExtra("state","2")
            startActivity(i)
    }
}

package com.example.anweshmishra.kotlinsmileyfaceview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import com.example.smileyfaceview.SmileyFaceView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var view : SmileyFaceView = SmileyFaceView.create(this)
        fullScreen()
        view.addOnSmileyFaceListener({
            Toast.makeText(this, "I am happy so I am visible", Toast.LENGTH_SHORT).show()
        }, {
            Toast.makeText(this, "I am sad so I left the room", Toast.LENGTH_SHORT).show()
        })
    }
}
fun MainActivity.fullScreen() {
    supportActionBar?.hide()
    window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
}

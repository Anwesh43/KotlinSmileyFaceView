package com.example.anweshmishra.kotlinsmileyfaceview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.smileyfaceview.SmileyFaceView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SmileyFaceView.create(this)
    }
}

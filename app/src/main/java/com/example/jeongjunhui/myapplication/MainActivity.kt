package com.example.jeongjunhui.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.bumptech.glide.Glide

class MainActivity : Activity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn = findViewById<Button>(R.id.Button01)
        val back_view = findViewById<ImageView>(R.id.gifview)
        //back_view.setImageResource(R.drawable.background1)
        Glide.with(this)
                .asGif()
                //.load("http://stupidgifs.com/wp-content/uploads/2016/07/The-wooden-spoon-has-its-effect.gif")
                .load(R.drawable.background1)
                .into(back_view!!)

        btn.setOnClickListener{
            //val str = "H: 68.0% T:25.3*C"
            //val seperate1 = str!!.split("H",":","%","T","*C"," ","[","]")[3]
            //val seperate2 = str!!.split("H",":","%","T","*C"," ","[","]")[7]

            //Log.d("Seperte1 : ","'$seperate1'")
            //Log.d("Seperte2 : ","'$seperate2'")
            val changeIntent = Intent(this, Main2Activity::class.java)
            startActivity(changeIntent)
        }
    }
    fun onClickBtn(view: View) {
        //textmsg.setText(set_msg.get)
        //val toast = Toast.makeText(applicationContext, mEt!!.text.toString(), Toast.LENGTH_SHORT)
        //toast.show()
        val changeIntent = Intent(this, Main2Activity::class.java)
        startActivity(changeIntent)
    }
}


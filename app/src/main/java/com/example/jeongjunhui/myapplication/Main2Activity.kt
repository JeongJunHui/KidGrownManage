package com.example.jeongjunhui.myapplication

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SizeReadyCallback
import kotlinx.android.synthetic.main.activity_main2.*
import java.io.*
import java.net.Socket
import kotlin.concurrent.thread

class Main2Activity : AppCompatActivity() {

    private var return_msg: String? = null
    private val serverIP = "192.168.0.215"
    private val serverPort = 9015   //포트번호
    private var mRun = true

    private var name_t : EditText ?= null
    private var age_t : EditText ?= null
    private var weight_t : EditText ?= null

    private var result_txt : TextView ?= null

    private var tallButton : Button ?= null
    private var saveButton : Button ?= null

    private var tall_flag = false
    private var dht_flag = true
    private  var end_flag = false

    private var tall: Float ?= null
    private var inetSocket: Socket? = null

    private val send_msg_ult : String ? = "ult"
    private val send_msg_end : String ? = "5"

    private var Tempo = 23.5
    private var Hum = 40.0
    private var humidity : Float ?= 0.0F
    private var temperature : Float ?= 0.0F

    private var tempview : ImageView ?= null
    private var humview : ImageView ?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        name_t = findViewById<EditText>(R.id.name_txt)
        age_t = findViewById<EditText>(R.id.age_txt)
        weight_t = findViewById<EditText>(R.id.weight_txt)

        val tcpThread = TCPclient()//mEt!!.text.toString())
        val thread = Thread(tcpThread)
        Thread.sleep(1000)
        thread.start()

        tempview = findViewById<ImageView>(R.id.tempView)
        humview = findViewById<ImageView>(R.id.humiView)

        result_txt = findViewById<TextView>(R.id.result_view)

        tallButton = findViewById<Button>(R.id.tall_btn)
        tallButton!!.setOnClickListener(View.OnClickListener {
            tall_flag = true
            try {
                Log.d("Tall Button", "들어온다")
                if (tall_flag) {
                    object : Thread() {
                        override fun run() {
                            super.run()
                            val out = PrintWriter(BufferedWriter(
                                    OutputStreamWriter(inetSocket!!.getOutputStream())), true)
                            out.println(send_msg_ult)
                        }
                    }.start()
                    /*
                    kotlin.concurrent.thread {
                        synchronized(this) {
                            Thread.currentThread()
                        }
                        val out = PrintWriter(BufferedWriter(
                                OutputStreamWriter(inetSocket!!.getOutputStream())), true)
                        out.println(send_msg_ult)
                    }.start()*/
                }
            }catch (e : Exception){
                Log.e("TCP", "C: Error2 Btn Click", e)
            }
        })

        saveButton = findViewById<Button>(R.id.save_btn)
        saveButton!!.setOnClickListener(View.OnClickListener {
            //mRun = false
            if(saveButton!!.text.toString().equals("결과 확인")){
                val changeIntent = Intent(this, Main3Activity::class.java)
                changeIntent.putExtra("name", name_t!!.text.toString())
                startActivity(changeIntent)
            }else {
                end_flag = true
                try {
                    if (end_flag) {
                        object : Thread() {
                            override fun run() {
                                super.run()
                                val out = PrintWriter(BufferedWriter(
                                        OutputStreamWriter(inetSocket!!.getOutputStream())), true)
                                out.println(send_msg_end)
                            }
                        }.start()
                         /*kotlin.concurrent.thread {
                             synchronized(this) {
                                 Thread.currentThread()
                             }
                            val out = PrintWriter(BufferedWriter(
                                    OutputStreamWriter(inetSocket!!.getOutputStream())), true)
                            out.println(send_msg_end)
                        }.start()*/
                    }
                } catch (e: Exception) {
                    Log.e("TCP", "C: Error2 Btn Save", e)
                }
                file_make()
                result_txt!!.setText("Name :" + name_t!!.text.toString() + "\nAGE : " + age_t!!.text.toString() + "\nWEIGHT : " + weight_t!!.text.toString() + "\nTall : " + tall.toString())
                Log.d("Save Button", "들어온다")
                saveButton!!.setText("결과 확인")
            }
        })
        //Glide.with(this).asBitmap().load(urlsH).into(humiView).onStart()
        //Glide.with(this).asBitmap().load(urlsT).into(tempView).onStart()

    }
    fun file_make() {
        try {
            val file_db = OutputStreamWriter(openFileOutput("TestDB2.txt", Activity.MODE_APPEND))
            file_db.write(name_t!!.text.toString() + "/")
            file_db.write(age_t!!.text.toString() + "/")
            file_db.write(weight_t!!.text.toString() + "/")
            file_db.write(tall.toString() + "/\n")
            Log.d("파일","입출력!!")
            file_db.flush()
            file_db.close()
        }catch (e : Exception){
            Log.d("File","write fail!!")
        }
    }
    private inner class TCPclient (): Runnable
    {//private val msg: String) : Runnable {
        private val send_msg_dht : String ? = "dht"
        override fun run() {
            // TODO Auto-generated method stub
            try {
                    Log.d("TCP", "C: Connecting...")
                //string_seperate()
                    inetSocket = Socket(serverIP, serverPort)
                //inetSocket.connect(socketAddr);
                    try {
                    //Log.d("TCP", "C: Sending: '$msg'")
                        while (mRun) {
                            if (tall_flag) {
                                val out = PrintWriter(BufferedWriter(
                                        OutputStreamWriter(inetSocket!!.getOutputStream())), true)
                                out.flush()
                                out.println(send_msg_ult)
                            }
                            if (dht_flag) {
                                val out = PrintWriter(BufferedWriter(
                                        OutputStreamWriter(inetSocket!!.getOutputStream())), true)
                                out.flush()
                                out.println(send_msg_dht)
                            }

                            Log.d("계속", "돌고있다")

                            val `in` = BufferedReader(
                                    InputStreamReader(inetSocket!!.getInputStream()))
                            return_msg = `in`.readLine()

                            Log.d("TCP", "C: Server send to me this message --&gt;" + return_msg!!)
                            val str = return_msg

                            if (tall_flag) {
                                val tall_separate = str!!.split("D", " ", ":", "cm")[4]
                                Log.d("Separate Tall", "tallsep : " + tall_separate)
                                tall = tall_separate.trim().toFloat()

                                tall_flag = false
                                //file_make()
                            }
                            if (dht_flag) {
                                var seperate1 = str!!.split("H", ":", "%", "T", "*C", " ", "[", "]")[3]
                                var seperate2 = str!!.split("H", ":", "%", "T", "*C", " ", "[", "]")[8]
                                Log.d("JJHSeparate1", "sep1 : " + seperate1 + "sep1 : " + seperate2)
                                //Log.d("JJHSeparate2", "sep1 : " + seperate2)

                                humidity = seperate1.trim().toFloat()
                                temperature = seperate2.trim().toFloat()

                                dht_flag = false

                                var urlsH : Int ? = 0
                                var urlsT : Int ? = 0

                                if(humidity!! > Hum){
                                    //urlsH = "http://cfile26.uf.tistory.com/image/254032445473ED6D085C9E"
                                    urlsH = R.drawable.humidity
                                }else{
                                    //urlsH = "http://cfile27.uf.tistory.com/image/241BB33855836B890B001A"
                                    urlsH = R.drawable.dry
                                }
                                if(temperature!! > Tempo){
                                    //urlsT = "http://cfile215.uf.daum.net/image/180DF2474EC48EC92A5379"
                                    urlsT = R.drawable.hottmp
                                }else{
                                    //urlsT = "https://ecurrent.fit.edu/files/2013/02/sun-in-hd.jpg"
                                    urlsT = R.drawable.coldtemp
                                }
                                humview!!.post({
                                    humview!!.setImageResource(urlsH!!)
                                    humview!!.invalidate()
                                })
                                tempview!!.post({
                                    tempview!!.setImageResource(urlsT!!)
                                    tempview!!.invalidate()
                                })
                            }
                        }
                    }
                    catch (e: Exception) {
                        Log.e("TCP", "C: Error1", e)
                    } finally {
                        //tall_flag = 0
                        if(end_flag){
                            val out = PrintWriter(BufferedWriter(
                                    OutputStreamWriter(inetSocket!!.getOutputStream())), true)
                            out.println(send_msg_end)
                            out.flush()
                            inetSocket!!.close()
                        }
                    }
            } catch (e: Exception) {
                Log.e("TCP", "C: Error2", e)
            }
        }
    }
}

package com.example.jeongjunhui.myapplication

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import java.io.BufferedReader
import java.io.InputStreamReader
import com.example.jeongjunhui.myapplication.R.id.graph
import java.io.RandomAccessFile

class Main3Activity : AppCompatActivity() {

    private var graph_view : GraphView ?= null
    private var explainTxt : TextView ?= null
    var i = 0
    var loop = 0
    var start : Double = 0.0
    private var size = 1000
    private var receive_name : String ?= null

    private val name = arrayOfNulls<String>(size)
    private val age = arrayOfNulls<Int>(size)
    private val weight = arrayOfNulls<Int>(size)
    private val tall = arrayOfNulls<Double?>(size)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        Log.d("새로운 액티비티", "들어온다")
        val intent = getIntent()
        receive_name = intent.getStringExtra("name")
        Log.d("ReceiveName"," : " + receive_name)
        explainTxt = findViewById<TextView>(R.id.explain_txt)

        readData()

        graph_view = findViewById<GraphView>(R.id.graph)
        makeGraph()
    }
    fun readData() {
        val file_rd = InputStreamReader(openFileInput("TestDB2.txt"))
        val br = BufferedReader(file_rd)
        var line = br.readLine()
        val all = StringBuilder()
        try {
            Log.d("File Read","여기도???")
            while (line != null){
                var returnMSG = line
                val pridata = returnMSG!!.split("/","\n")[0]
                Log.d("파일 읽기","여기도???" + pridata)
                if (pridata.equals(receive_name)){
                    all.append(line + "\n")
                    val lineList = mutableListOf<String>()
                    Log.d("파일 읽기","여기도도도?")
                    name!![i] = returnMSG!!.split("/","\n")[0]
                    age!![i] = returnMSG!!.split("/","\n")[1].toInt()
                    weight!![i] = returnMSG!!.split("/","\n")[2].toInt()
                    tall!![i] = returnMSG!!.split("/","\n")[3].toDouble()
                    Log.d("FileRead","Name : " + name!![i] + ", AGE : " + age!![i].toString() + ", Tall : "+ tall!![i].toString())
                    i++
                }
                line = br.readLine()
            }
            br.close()
            file_rd.close()
        }catch (e : Exception){
            Log.e("File", "Read File", e)
        }
        finally {
            explainTxt!!.setText(all)
        }
    }
    fun makeGraph() {
        val series = LineGraphSeries<DataPoint>()
        if(i < 5){
            for (loop in 1 until i-1){
                series!!.appendData(DataPoint(start, tall[loop]!!),true,200)
                start = start + 0.5
            }
        }else{
            for (loop in i-5 until i-1){
                series!!.appendData(DataPoint(start, tall[loop]!!),true,200)
                start = start + 0.5
            }
        }

        graph_view!!.addSeries(series)
    }
}

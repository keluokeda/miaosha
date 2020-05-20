package com.ke.miaosha

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    lateinit var disposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        val dateList = listOf<Date>(
                simpleDateFormat.parse("2020-05-20 11:00:00")!!,
                simpleDateFormat.parse("2020-05-20 12:00:00")!!,
                simpleDateFormat.parse("2020-05-20 12:30:00")!!,
                simpleDateFormat.parse("2020-05-21 12:30:00")!!

        )

        val adapter = Adapter(dateList)
        recycler_view.adapter = adapter

        disposable = Observable.interval(0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    adapter.notifyDataSetChanged()
                }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}


class Adapter(private val dateList: List<Date>) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = dateList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.refreshContent(dateList[position])
    }

}


class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val text = itemView.findViewById<TextView>(R.id.text)


    fun refreshContent(date: Date) {

        //毫秒
        val interval = date.time - Date().time

        val hour = interval / (1000 * 60 * 60)

        val minutes = (interval - hour * 60 * 60 * 1000) / (1000 * 60)

        val second = (interval - hour * 60 * 60 * 1000-minutes * 60 * 1000) / 1000
        text.text = "$hour : $minutes : $second"


    }
}
package com.sunnyweather.android.ui.place

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sunnyweather.android.R
import com.sunnyweather.android.logic.PlaceViewModel

class PlaceFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(this).get(PlaceViewModel::class.java)
    }

    lateinit var adapter: PlaceAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_place, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val layoutManager = LinearLayoutManager(activity)

        val recyclerView : RecyclerView = activity!!.findViewById(R.id.recyclerView)
        val searchPlaceEdit : EditText = activity!!.findViewById(R.id.searchPlaceEdit)
        val bgImageView : ImageView = activity!!.findViewById(R.id.bgImageView)

        // 设置 RecyclerView 的布局和适配器
        recyclerView.layoutManager = layoutManager
        adapter = PlaceAdapter(this, viewModel.placeList)
        recyclerView.adapter = adapter

        // 监听输入框的变化
        searchPlaceEdit.addTextChangedListener {
            val content = it.toString()
            if(content.isNotEmpty()) {
                viewModel.searchPlaces(content)
            } else {
                recyclerView.visibility = View.GONE
                bgImageView.visibility = View.VISIBLE
                viewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            }
        }

         viewModel.placesLiveData.observe(this, Observer {
             val places = it.getOrNull()
             if(places != null) {
                 recyclerView.visibility = View.VISIBLE
                 bgImageView.visibility = View.GONE
                 viewModel.placeList.clear()
                 viewModel.placeList.addAll(places)
                 adapter.notifyDataSetChanged()
             } else {
                 Toast.makeText(activity, "未能查询到任何地点", Toast.LENGTH_SHORT).show()
                 it.exceptionOrNull()?.printStackTrace()
             }
         })
    }

}
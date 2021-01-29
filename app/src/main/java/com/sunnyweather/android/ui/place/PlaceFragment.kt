package com.sunnyweather.android.ui.place

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sunnyweather.android.R
import com.sunnyweather.android.ui.weather.WeatherActivity

class PlaceFragment : Fragment() {

    val viewModel by lazy {
        ViewModelProvider(this).get(PlaceViewModel::class.java)
    }

    lateinit var adapter: PlaceAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_place, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(viewModel.isPlaceSaved()) {
            val place = viewModel.getSavedPlace()
            val intent = Intent(context, WeatherActivity::class.java).apply {
                putExtra("location_lng", place.location.lng)
                putExtra("location_lat", place.location.lat)
                putExtra("place_name", place.name)
            }
            startActivity(intent)
            activity?.finish()
            return
        }

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

         viewModel.placesLiveData.observe(viewLifecycleOwner, {
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
package com.example.weatherapp

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.weatherapp.databinding.FragmentFirstBinding


class FirstFragment : Fragment() {

    private lateinit var binding: FragmentFirstBinding

    lateinit var monthsRV : RecyclerView

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_first, container, false)
        monthsRV = binding.monthsRV
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_addObservationFragment)
        }
        binding.openDays.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        fetchMonths()
    }

    //Funktio, joka hakee kuukaudet recyclerviewiin
    private fun fetchMonths(){
        val rf = Retrofit.Builder()
                .baseUrl(RetrofitInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()

        val api = rf.create(RetrofitInterface::class.java)

        api.fetchAllMonths().enqueue(object : Callback<List<MonthsModel>> {
            override fun onResponse(call: Call<List<MonthsModel>>, response: Response<List<MonthsModel>>) {
                monthsRV.apply {
                    layoutManager = LinearLayoutManager(activity)
                    adapter = MonthsAdapter(response.body()!!)
                }
            }

            override fun onFailure(call: Call<List<MonthsModel>>, t: Throwable) {
                println("Jotaki erroria puskee: " + t.toString())
            }
        })
    }
}
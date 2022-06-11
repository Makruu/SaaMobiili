package com.example.weatherapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.FragmentSecondBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SecondFragment : Fragment(), DaysAdapter.OnItemClickListener {

    private lateinit var binding: FragmentSecondBinding

    lateinit var daysRV : RecyclerView

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_second, container, false)
        daysRV = binding.daysRV
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_ViewObservationFragment)
        }
        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        //Kovakoodattuna hakee kaikki huhtikuun havainnot
        fetchDays("2021-04")
    }

    //Recyclerviewin dynaaminen onClick, ei toimi vielä
    override fun onItemClick(position: Int) {
        println("Tämä ei toimi vielä")
    }

    //Funktio, joka hakee päivät recyclerviewiin
    private fun fetchDays(paivat: String){
        val rf = Retrofit.Builder()
            .baseUrl(RetrofitInterface.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()

        val api = rf.create(RetrofitInterface::class.java)

        api.fetchMonthDays(paivat).enqueue(object : Callback<List<DaysModel>> {
            override fun onResponse(call: Call<List<DaysModel>>, response: Response<List<DaysModel>>) {
                daysRV.apply {
                    layoutManager = LinearLayoutManager(activity)
                    adapter = DaysAdapter(response.body()!!, this@SecondFragment)
                }
            }

            override fun onFailure(call: Call<List<DaysModel>>, t: Throwable) {
                println("Jotaki erroria puskee: " + t.toString())
            }
        })
    }
}
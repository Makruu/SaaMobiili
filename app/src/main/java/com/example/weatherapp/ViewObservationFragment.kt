package com.example.weatherapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ViewObservationFragment : Fragment() {

    lateinit var lampotila: TextView
    lateinit var kosteus: TextView
    lateinit var ilmanpaine: TextView
    lateinit var vapaasana: TextView
    lateinit var aika: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_view_observation, container, false)
        lampotila = view.findViewById(R.id.observationTemperature)
        kosteus = view.findViewById(R.id.observationHumidity)
        ilmanpaine = view.findViewById(R.id.observationAirpressure)
        vapaasana = view.findViewById(R.id.observationOpenWord)
        aika = view.findViewById(R.id.viewObservationTitle)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchObservation()
    }

    private fun fetchObservation() {

        var observationlist: List<ObservationModel>

        val rf = Retrofit.Builder()
            .baseUrl(RetrofitInterface.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()

        val api = rf.create(RetrofitInterface::class.java)

        api.fetchObservation(48).enqueue(object : Callback<List<ObservationModel>> {
            override fun onResponse(call: Call<List<ObservationModel>>, response: Response<List<ObservationModel>>) {
                observationlist = response.body() as List<ObservationModel>
                val pvm : String = observationlist[0].date

                lampotila.text = observationlist[0].temperature
                kosteus.text = observationlist[0].humidity
                ilmanpaine.text = observationlist[0].air_pressure
                vapaasana.text = observationlist[0].note
                aika.text = getString(R.string.observation_datetime, pvm.substring(8,10), pvm.substring(4,8),
                    pvm.substring(0,4), pvm.substring(11,16))
            }

            override fun onFailure(call: Call<List<ObservationModel>>, t: Throwable) {
                println("Jotaki erroria puskee: " + t.toString())
            }
        })
    }
}
package com.example.weatherapp

import android.text.Editable
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddObservationViewModel : ViewModel() {

    var lampotila = MutableLiveData<Editable>()
    var kosteus = Editable.Factory.getInstance().newEditable(" ")
    var ilmanpaine = Editable.Factory.getInstance().newEditable(" ")
    var note = MutableLiveData<Editable>()

    init {
        Log.i("AddObservationViewModel", "AddObservationViewModel created.")
        note.value = Editable.Factory.getInstance().newEditable(" ")
        lampotila.value = Editable.Factory.getInstance().newEditable(" ")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("AddObservationViewModel", "AddObservationViewModel destroyed")
    }

    fun testi() {
        println("Testataan viewModelin kutsumista UI Controllerista")
    }

    //Funktio havainnon tallentamiseen
    fun submitObservation(temperature: String, humidity: String, pressure: String, note: String) {

        val rf = Retrofit.Builder()
                .baseUrl(RetrofitInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()

        val api = rf.create(RetrofitInterface::class.java)
        val jsonObject = JSONObject()
        jsonObject.put("temperature", temperature.toFloat())
        jsonObject.put("humidity", humidity.toFloat())
        jsonObject.put("air_pressure", pressure.toFloat())
        jsonObject.put("note", note)

        val jsonObjectString = jsonObject.toString()

        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        CoroutineScope(Dispatchers.IO).launch {
            val response = api.createObservation(requestBody)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(
                            response.body()
                                    ?.string()
                    )
                    println("Läpi meni: " + prettyJson)
                } else {
                    println("No hups, ei mennyt läpi")
                }
            }
        }
    }

    //Funktio, joka hakee JSON datan käyttäen RetrofitInterface- ja RuuviModel-tiedostoja
    fun fetchJSON() {

        var ruuvilist: List<RuuviModel>

        val rf = Retrofit.Builder()
                .baseUrl(RetrofitInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()

        val api = rf.create(RetrofitInterface::class.java)
        val call = api.ruuvi

        call?.enqueue(object: Callback<List<RuuviModel?>?> {
            override fun onResponse(
                    call: Call<List<RuuviModel?>?>,
                    response: Response<List<RuuviModel?>?>
            ) {
                ruuvilist = response.body() as List<RuuviModel>

                lampotila.value = Editable.Factory.getInstance().newEditable(ruuvilist!![0].temperature.toString())
                kosteus = Editable.Factory.getInstance().newEditable(ruuvilist!![0].humidity.toString())
                ilmanpaine = Editable.Factory.getInstance().newEditable(ruuvilist!![0].pressure.toString())



                itsTooDamnHot(ruuvilist!![0].temperature.toString())
            }

            override fun onFailure(call: Call<List<RuuviModel?>?>, t: Throwable) {
                println("Jotaki erroria puskee: " + t.toString())
            }
        })
    }

    // Tässä maailman päälleliimatuin liveDatahommeli
    fun itsTooDamnHot (lampotila: String) {
        if (lampotila.toFloat() > 5) {
            note.value = Editable.Factory.getInstance().newEditable(" Liian kuuma! ")
        }
    }
}
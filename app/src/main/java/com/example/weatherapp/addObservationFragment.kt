package com.example.weatherapp

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.databinding.FragmentAddObservationBinding
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


class addObservationFragment : Fragment() {

    private lateinit var binding: FragmentAddObservationBinding

    // Alustetaan vastaava viewModel
    private lateinit var viewModel: AddObservationViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_observation, container, false)

        // Kutsutaan vastaavaa vievModelia
        println("Called ViewModelProvider.get")
        viewModel = ViewModelProvider(this).get(AddObservationViewModel::class.java)

        /** Setting up LiveData observation relationship **/
        viewModel.note.observe(viewLifecycleOwner, Observer { upDatedOpenWord ->
            binding.openWordEditText.text = upDatedOpenWord
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Tämä koodi hakee JSON datan käyttäen RetrofitInterface- ja RuuviModel-tiedostoja
        viewModel.fetchJSON()

        //Tallenna-nappi
         binding.submitButton.setOnClickListener {
            viewModel.submitObservation(
                    binding.temperatureEditText.text.toString(),
                    binding.humidityEditText.text.toString(),
                    binding.airPressureEditText.text.toString(),
                    binding.openWordEditText.text.toString())
            activity?.onBackPressed()
        }

        // Kuvanottonappi
        binding.btnTakePicture.setOnClickListener { dispatchTakePictureIntent() }

        // Arvojen haku Ruuvista
        binding.fetchButton.setOnClickListener { updateFields() }
    }

    fun updateFields() {
        binding.temperatureEditText.text = viewModel.lampotila.value
        binding.airPressureEditText.text = viewModel.ilmanpaine
        binding.humidityEditText.text = viewModel.kosteus
    }

    val REQUEST_IMAGE_CAPTURE = 1 // Vakio kuvan näyttämistä varten

    // Käsitellään kuvan ottoa
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            println("Virhe kuvaa otettaessa.")
        }
    }

    // Näytetään kuva näkymässä
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data!!.extras!!.get("data") as Bitmap
            binding.imageView2.setImageBitmap(imageBitmap)
        }
    }

/*
    //Funktio havainnon tallentamiseen
    private fun submitObservation() {

        val rf = Retrofit.Builder()
                .baseUrl(RetrofitInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()

        val api = rf.create(RetrofitInterface::class.java)
        val jsonObject = JSONObject()
            jsonObject.put("temperature", binding.temperatureEditText.text.toString().toFloat())
            jsonObject.put("humidity", binding.humidityEditText.text.toString().toFloat())
            jsonObject.put("air_pressure", binding.airPressureEditText.text.toString().toFloat())
            jsonObject.put("note", binding.openWordEditText.text.toString())

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
    }*/


    //Funktio, joka hakee JSON datan käyttäen RetrofitInterface- ja RuuviModel-tiedostoja
    /*
    private fun fetchJSON() {

        println("fetchJSONista")
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
                val ruuvilist : List<RuuviModel>? = response.body() as List<RuuviModel>

                //Nämä kolme riviä asettaa ruuvin datan textvieweihin
                temperature.text = Editable.Factory.getInstance().newEditable(ruuvilist!![0].temperature.toString())
                humidity.text = Editable.Factory.getInstance().newEditable(ruuvilist!![0].humidity.toString())
                pressure.text = Editable.Factory.getInstance().newEditable(ruuvilist!![0].pressure.toString())
            }

            override fun onFailure(call: Call<List<RuuviModel?>?>, t: Throwable) {
                println("Jotaki erroria puskee: " + t.toString())
            }

        })
    }*/
}
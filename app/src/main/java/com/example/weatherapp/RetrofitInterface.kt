package com.example.weatherapp

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RetrofitInterface {
    @get:GET("ruuvi")
    val ruuvi : Call<List<RuuviModel?>?>?

    @GET("havainto/kuukausi")
    //val kuukausi : Call<List<MonthsModel>>
    fun fetchAllMonths(): Call<List<MonthsModel>>

    @GET("havainto/paivat/{paivat}")
    fun fetchMonthDays(@Path("paivat")paivat: String): Call<List<DaysModel>>

    @GET("havainto/id/{id}")
    fun fetchObservation(@Path("id")id: Int): Call<List<ObservationModel>>

    @POST("havainto")
    suspend fun createObservation(@Body requestBody: RequestBody): Response<ResponseBody>

    companion object {
        const val BASE_URL = "http:makruu.com:3000/"
    }
}
package com.marsuch.weatherappforeternal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.*
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marsuch.weatherappforeternal.network.WeatherApi
import com.marsuch.weatherappforeternal.network.dataclasses.WeatherInfo
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val _status = MutableLiveData<String?>()

        val status: LiveData<String?> = _status
        val api : String = "33aa634c216259f797f35e862f073b40"

        val spinner = findViewById<Spinner>(R.id.cities)
        val result = findViewById<TextView>(R.id.result)
        val cities = arrayOf("České Budějovice", "New York", "Sydney")
        val cnt = findViewById<EditText>(R.id.NumberOfDays)
        val button = findViewById<Button>(R.id.button)

        spinner.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cities)

        button.setOnClickListener {
            val selected = spinner.selectedItem as String

            var lat: String = ""
            var lon: String = ""
            if (selected == "České Budějovice") {
                lat = "48.97378881915517"
                lon = "14.476120512906416"
            } else if (selected == "New York") {
                lat = "40.79052384606425"
                lon = "-73.95908688800822"
            } else if (selected == "Sydney") {
                lat = "-33.8470241774331"
                lon = "151.0624326592654"
            }

            if(!TextUtils.isEmpty(cnt.text.toString())){
                lifecycleScope.launch{
                    WeatherApi.retrofitService.getWeatherData(lat, lon, api, "metric", cnt.text.toString()).enqueue(object: Callback<WeatherInfo> {
                        override fun onResponse(call: Call<WeatherInfo>, response: Response<WeatherInfo>) {
                           result.text = response.body().toString()
                            Log.d("Full response: ", response.body().toString())
                        }

                        override fun onFailure(call: Call<WeatherInfo>, t: Throwable) {
                            Log.d("Failure: ", t.message.toString())
                        }

                    })
                }
            }
        }

    }
}
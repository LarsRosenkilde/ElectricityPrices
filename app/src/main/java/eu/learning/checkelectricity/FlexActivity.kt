package eu.learning.checkelectricity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.flow.combine
import org.jsoup.Jsoup
import java.io.IOException

@Suppress("DEPRECATION")
class FlexActivity : AppCompatActivity() {
    private lateinit var textViewFlexW: TextView
    private lateinit var textViewFlexE: TextView
    lateinit var button: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "CheckElectricity"
        textViewFlexW = findViewById(R.id.price_west)
        textViewFlexE = findViewById(R.id.price_east)
        button = findViewById(R.id.btnView)
        button.setOnClickListener {
            WebScratch().execute()
        }
    }
    @SuppressLint("StaticFieldLeak")
    inner class WebScratch : AsyncTask<Void, Void, Void>() {
        private lateinit var flexW: String
        private lateinit var flexE: String
        val regex: Regex = """([0-9])\w+,[0-9]\w""".toRegex()
        override fun doInBackground(vararg params: Void): Void? {
            try {
                val document =  Jsoup.connect("https://norlys.dk/kundeservice/el/gaeldende-elpriser/").get()
                flexW = document.select("#elprodukter > div > div > div > div > table:nth-child(5) > tbody > tr:nth-child(2) > td:nth-child(2)").toString()
                flexW = regex.find(flexW)!!.value + " øre/kWh"
                flexE = document.select("#elprodukter > div > div > div > div > table:nth-child(5) > tbody > tr:nth-child(3) > td:nth-child(2)").toString()
                flexE = regex.find(flexE)!!.value + " øre/kWh"
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }
        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            textViewFlexW.text = flexW
            textViewFlexE.text = flexE
        }
    }
}
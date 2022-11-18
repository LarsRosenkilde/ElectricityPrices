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
class MainActivity : AppCompatActivity() {
    //private lateinit var textViewPuljeW: TextView
    //private lateinit var textViewKombiW: TextView
    private lateinit var textViewFlexW: TextView
    //private lateinit var textViewPuljeE: TextView
    //private lateinit var textViewKombiE: TextView
    private lateinit var textViewFlexE: TextView
    lateinit var button: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "KotlinApp"
        //textViewPuljeW = findViewById(R.id.puljeWest)
        //textViewKombiW = findViewById(R.id.combiWest)
        textViewFlexW = findViewById(R.id.price_west)
        //textViewPuljeE = findViewById(R.id.puljeEast)
        //textViewKombiE = findViewById(R.id.combiEast)
        textViewFlexE = findViewById(R.id.price_east)
        button = findViewById(R.id.btnView)
        button.setOnClickListener {
            WebScratch().execute()
        }
    }
    @SuppressLint("StaticFieldLeak")
    inner class WebScratch : AsyncTask<Void, Void, Void>() {
        //private lateinit var puljeW: String
        //private lateinit var kombiW: String
        private lateinit var flexW: String
        //private lateinit var puljeE: String
        //private lateinit var kombiE: String
        private lateinit var flexE: String
        val regex: Regex = """([0-9])\w+,[0-9]\w""".toRegex()
        override fun doInBackground(vararg params: Void): Void? {
            try {
                val document =  Jsoup.connect("https://norlys.dk/kundeservice/el/gaeldende-elpriser/").get()
                //puljeW = document.select("#elprodukter > div > div > div > div > table:nth-child(1) > tbody > tr:nth-child(2) > td:nth-child(2)").toString()
                //puljeW = "Pulje El:\n" + regex.find(puljeW)!!.value + " øre/kWh"
                //kombiW = document.select("#elprodukter > div > div > div > div > table:nth-child(3) > tbody > tr:nth-child(2) > td:nth-child(2)").toString()
                //kombiW = "Kombi El:\n" + regex.find(kombiW)!!.value + " øre/kWh"
                flexW = document.select("#elprodukter > div > div > div > div > table:nth-child(5) > tbody > tr:nth-child(2) > td:nth-child(2)").toString()
                flexW = regex.find(flexW)!!.value + " øre/kWh"

                //puljeE = document.select("#elprodukter > div > div > div > div > table:nth-child(1) > tbody > tr:nth-child(3) > td:nth-child(2)").toString()
                //puljeE = "Pulje El:\n" + regex.find(puljeE)!!.value + " øre/kWh"
                //kombiE = document.select("#elprodukter > div > div > div > div > table:nth-child(3) > tbody > tr:nth-child(3) > td:nth-child(2)").toString()
                //kombiE = "Kombi El:\n" + regex.find(kombiE)!!.value + " øre/kWh"
                flexE = document.select("#elprodukter > div > div > div > div > table:nth-child(5) > tbody > tr:nth-child(3) > td:nth-child(2)").toString()
                flexE = regex.find(flexE)!!.value + " øre/kWh"
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }
        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            //textViewPuljeW.text = puljeW
            //textViewKombiW.text = kombiW
            textViewFlexW.text = flexW
            //textViewPuljeE.text = puljeE
            //textViewKombiE.text = kombiE
            textViewFlexE.text = flexE
        }
    }
}
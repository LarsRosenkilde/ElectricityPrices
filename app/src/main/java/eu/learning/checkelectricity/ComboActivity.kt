package eu.learning.checkelectricity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import eu.learning.checkelectricity.databinding.ActivityComboBinding
import org.jsoup.Jsoup
import java.io.IOException

@Suppress("DEPRECATION")
class ComboActivity : AppCompatActivity() {
    private lateinit var binding: ActivityComboBinding
    private lateinit var textViewComboW: TextView
    private lateinit var textViewComboE: TextView
    lateinit var button: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComboBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "CheckElectricity"
        textViewComboW = findViewById(R.id.price_west)
        textViewComboE = findViewById(R.id.price_east)
        button = findViewById(R.id.btnView)
        button.setOnClickListener {
            WebScratch().execute()
        }
        WebScratch().execute()
    }
    @SuppressLint("StaticFieldLeak")
    inner class WebScratch : AsyncTask<Void, Void, Void>() {
        private lateinit var comboW: String
        private lateinit var comboE: String
        private val regex: Regex = """([0-9])\w+,[0-9]\w""".toRegex() // 100,00
        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg params: Void): Void? {
            try {
                val document =  Jsoup.connect("https://norlys.dk/kundeservice/el/gaeldende-elpriser/").get()
                comboW = document.select("#elprodukter > div > div > div > div > table:nth-child(3) > tbody > tr:nth-child(2) > td:nth-child(2)").toString()
                comboW = regex.find(comboW)!!.value + " øre/kWh"
                comboE = document.select("#elprodukter > div > div > div > div > table:nth-child(3) > tbody > tr:nth-child(3) > td:nth-child(2)").toString()
                comboE = regex.find(comboE)!!.value + " øre/kWh"
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }
        @Deprecated("Deprecated in Java")
        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            textViewComboW.text = comboW
            textViewComboE.text = comboE
        }
    }
}
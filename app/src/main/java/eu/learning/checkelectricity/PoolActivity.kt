package eu.learning.checkelectricity

import org.jsoup.Jsoup
import android.os.Bundle
import java.io.IOException
import android.os.AsyncTask
import android.content.Intent
import android.widget.TextView
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import eu.learning.checkelectricity.databinding.ActivityPoolBinding

@Suppress("DEPRECATION")
class PoolActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPoolBinding
    private lateinit var textViewPoolW: TextView
    private lateinit var textViewPoolE: TextView

    private lateinit var prices: MutableMap<String, String>

    private lateinit var intentFlex: Intent
    private lateinit var intentCombo: Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPoolBinding.inflate(layoutInflater)
        title = "CheckElectricity"
        intentFlex = Intent(this, FlexActivity::class.java)
        intentCombo = Intent(this, ComboActivity::class.java)
        setContentView(binding.root)

        textViewPoolW = findViewById(R.id.price_west)
        textViewPoolE = findViewById(R.id.price_east)

        prices = mutableMapOf(
            "priceW" to "",
            "priceE" to "",
        )

        binding.flexButton.setOnClickListener {
            startActivity(intentFlex)
        }
        binding.comboButton.setOnClickListener {
            startActivity(intentCombo)
        }
        binding.poolButton.isPressed = true
        WebScratch().execute()
    }
    @SuppressLint("StaticFieldLeak")
    inner class WebScratch : AsyncTask<Void, Void, Void>() {
        private val priceW = "#elprodukter > div > div > div > div > table:nth-child(1) > tbody > tr:nth-child(2) > td:nth-child(2)"
        private val priceE = "#elprodukter > div > div > div > div > table:nth-child(1) > tbody > tr:nth-child(3) > td:nth-child(2)"
        private val regex: Regex = """([0-9])\w+,[0-9]\w""".toRegex()
        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg params: Void): Void? {
            try {
                val document =  Jsoup.connect("https://norlys.dk/kundeservice/el/gaeldende-elpriser/").get()
                prices["priceW"] = regex.find(document.select(priceW).toString())!!.value + " øre/kWh"
                prices["priceE"] = regex.find(document.select(priceE).toString())!!.value + " øre/kWh"
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }
        @Deprecated("Deprecated in Java")
        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            textViewPoolW.text = prices["priceW"]
            textViewPoolE.text = prices["priceE"]
        }
    }
}
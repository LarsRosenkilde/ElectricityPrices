package eu.learning.checkelectricity

import org.jsoup.Jsoup
import android.os.Bundle
import java.io.IOException
import android.os.AsyncTask
import android.content.Intent
import android.widget.TextView
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import eu.learning.checkelectricity.databinding.ActivityComboBinding

@Suppress("DEPRECATION")
class ComboActivity : AppCompatActivity() {
    private lateinit var binding: ActivityComboBinding
    private lateinit var textViewComboW: TextView
    private lateinit var textViewComboE: TextView

    private lateinit var prices: MutableMap<String, String>

    private lateinit var intentFlex: Intent
    private lateinit var intentPool: Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComboBinding.inflate(layoutInflater)
        title = "CheckElectricity"
        intentFlex = Intent(this, FlexActivity::class.java)
        intentPool = Intent(this, PoolActivity::class.java)
        setContentView(binding.root)
        textViewComboW = findViewById(R.id.price_west)
        textViewComboE = findViewById(R.id.price_east)

        prices = mutableMapOf(
            "priceW" to "",
            "priceE" to "",
        )

        binding.flexButton.setOnClickListener {
            startActivity(intentFlex)
        }
        binding.poolButton.setOnClickListener {
            startActivity(intentPool)
        }
        binding.comboButton.isPressed = true
        WebScratch().execute()
    }
    @SuppressLint("StaticFieldLeak")
    inner class WebScratch : AsyncTask<Void, Void, Void>() {
        private val priceW = "#elprodukter > div > div > div > div > table:nth-child(3) > tbody > tr:nth-child(2) > td:nth-child(2)"
        private val priceE = "#elprodukter > div > div > div > div > table:nth-child(3) > tbody > tr:nth-child(3) > td:nth-child(2)"
        private val regex: Regex = """([0-9])\w+,[0-9]\w""".toRegex() // 100,00
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
            textViewComboW.text = prices["priceW"]
            textViewComboE.text = prices["priceE"]
        }
    }
}
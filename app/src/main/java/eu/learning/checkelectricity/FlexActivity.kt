package eu.learning.checkelectricity

import org.jsoup.Jsoup
import android.os.Bundle
import java.io.IOException
import android.os.AsyncTask
import android.content.Intent
import android.widget.TextView
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import eu.learning.checkelectricity.databinding.ActivityFlexBinding
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class FlexActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFlexBinding
    private lateinit var textViewFlexW: TextView
    private lateinit var textViewFlexE: TextView

    private lateinit var prices: MutableMap<String, String>
    private lateinit var wOldPrice0: TextView
    private lateinit var sharedPreference: SharedPreferences

    private lateinit var intentPool: Intent
    private lateinit var intentCombo: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFlexBinding.inflate(layoutInflater)
        title = "CheckElectricity"
        intentPool = Intent(this, PoolActivity::class.java)
        intentCombo = Intent(this, ComboActivity::class.java)
        setContentView(binding.root)

        textViewFlexW = findViewById(R.id.price_west)
        textViewFlexE = findViewById(R.id.price_east)

        wOldPrice0 = findViewById(R.id.oldWestPrice0)
        sharedPreference = getSharedPreferences("savedPrices", Context.MODE_PRIVATE)
        prices = mutableMapOf(
            "priceW" to "",
            "priceE" to "",
        )

        binding.comboButton.setOnClickListener {
            startActivity(intentCombo)
        }
        binding.poolButton.setOnClickListener {
            startActivity(intentPool)
        }
        binding.flexButton.isPressed = true
        WebScratch().execute()
    }
    @SuppressLint("StaticFieldLeak")
    inner class WebScratch : AsyncTask<Void, Void, Void>() {
        private val priceW = "#elprodukter > div > div > div > div > table:nth-child(5) > tbody > tr:nth-child(2) > td:nth-child(2)"
        private val priceE = "#elprodukter > div > div > div > div > table:nth-child(5) > tbody > tr:nth-child(3) > td:nth-child(2)"

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg params: Void): Void? {
            try {
                val regex: Regex = """([0-9])\w+,[0-9]\w""".toRegex()
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
            readData()
            textViewFlexW.text = prices["priceW"]
            textViewFlexE.text = prices["priceE"]
        }

        @SuppressLint("SimpleDateFormat")
        private fun saveData() {
            val dateFormat = SimpleDateFormat("dd/M/yyyy")
            var result: String = dateFormat.format(Date())
            for (price in prices) {
                result += ",${price.value}"
            }
            result += '\n'
            val editor: SharedPreferences.Editor = sharedPreference.edit()
            editor.apply {
                putString("flexW", result)
            }.apply()
        }

        private fun readData() {
            val savedString: String = sharedPreference.getString("flexW", "defaultPrice")  ?: "Preference Empty!"
            wOldPrice0.text = savedString
        }
    }
}

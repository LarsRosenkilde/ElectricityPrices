package eu.learning.checkelectricity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import eu.learning.checkelectricity.databinding.ActivityMainBinding
import org.jsoup.Jsoup
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var intentFlex: Intent
    private lateinit var sharedPreference: SharedPreferences
    public lateinit var prices: MutableMap<String, String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        sharedPreference = getSharedPreferences("savedPrices", Context.MODE_PRIVATE)
        title = "CheckElectricity"
        intentFlex = Intent(this, FlexActivity::class.java)
        prices = mutableMapOf(
            "poolW" to "",
            "poolE" to "",
            "flexW" to "",
            "flexE" to "",
            "combW" to "",
            "combE" to ""
        )
        setContentView(binding.root)
        startActivity(intentFlex)
    }

    @SuppressLint("StaticFieldLeak")
    inner class WebScratch : AsyncTask<Void, Void, Void>() {
        private val regex: Regex = """([0-9])\w+,[0-9]\w""".toRegex() // <num><comma><num> 111,99
        private val poolElemW =
            "#elprodukter > div > div > div > div > table:nth-child(1) > tbody > tr:nth-child(2) > td:nth-child(2)"
        private val poolElemE =
            "#elprodukter > div > div > div > div > table:nth-child(1) > tbody > tr:nth-child(3) > td:nth-child(2)"
        private val flexElemW =
            "#elprodukter > div > div > div > div > table:nth-child(5) > tbody > tr:nth-child(2) > td:nth-child(2)"
        private val flexElemE =
            "#elprodukter > div > div > div > div > table:nth-child(5) > tbody > tr:nth-child(3) > td:nth-child(2)"
        private val combElemW =
            "#elprodukter > div > div > div > div > table:nth-child(3) > tbody > tr:nth-child(2) > td:nth-child(2)"
        private val combElemE =
            "#elprodukter > div > div > div > div > table:nth-child(3) > tbody > tr:nth-child(3) > td:nth-child(2)"

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg params: Void): Void? {
            try {
                val document =
                    Jsoup.connect("https://norlys.dk/kundeservice/el/gaeldende-elpriser/").get()
                prices["poolW"] =
                    regex.find(document.select(poolElemW).toString())!!.value + " øre/kWh"
                prices["poolE"] =
                    regex.find(document.select(poolElemE).toString())!!.value + " øre/kWh"
                prices["flexW"] =
                    regex.find(document.select(flexElemW).toString())!!.value + " øre/kWh"
                prices["flexE"] =
                    regex.find(document.select(flexElemE).toString())!!.value + " øre/kWh"
                prices["combW"] =
                    regex.find(document.select(combElemW).toString())!!.value + " øre/kWh"
                prices["combE"] =
                    regex.find(document.select(combElemE).toString())!!.value + " øre/kWh"
            } catch (e: IOException) {
                prices.forEach { entry ->
                    prices[entry.key] = e.stackTraceToString()
                }
            }
            return null
        }

        @Deprecated("Deprecated in Java")
        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            saveData()
        }

        @SuppressLint("SimpleDateFormat")
        private fun saveData() {
            val dateFormat = SimpleDateFormat("dd/M/yyyy")
            var result: String = dateFormat.format(Date())
            for (price in prices) {
                result += ",${price.value}"
            }
            result += '\n'

            result = "Testing!!!"

            val editor: SharedPreferences.Editor = sharedPreference.edit()
            editor.putString("flexW", "Test Price")
            editor.apply()
            /*editor.apply {
                putString("STRING_KEY", result)
            }.apply() */
        }
    }
}

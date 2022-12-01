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
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import eu.learning.checkelectricity.databinding.ActivityFlexBinding
import io.github.farshidroohi.ChartEntity
import io.github.farshidroohi.LineChart
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class FlexActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFlexBinding
    private lateinit var textViewFlexW: TextView
    private lateinit var textViewFlexE: TextView

    private lateinit var prices: MutableMap<String, String>
    private lateinit var sharedPreference: SharedPreferences

    private lateinit var date0: TextView
    private lateinit var date1: TextView
    private lateinit var date2: TextView
    private lateinit var date3: TextView
    private lateinit var date4: TextView
    private lateinit var wOldPrice0: TextView
    private lateinit var eOldPrice0: TextView
    private lateinit var wOldPrice1: TextView
    private lateinit var eOldPrice1: TextView
    private lateinit var wOldPrice2: TextView
    private lateinit var eOldPrice2: TextView
    private lateinit var wOldPrice3: TextView
    private lateinit var eOldPrice3: TextView
    private lateinit var wOldPrice4: TextView
    private lateinit var eOldPrice4: TextView

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

        date0 = findViewById(R.id.oldDatePrice0)
        wOldPrice0 = findViewById(R.id.oldWestPrice0)
        eOldPrice0 = findViewById(R.id.oldEastPrice0)
        date1 = findViewById(R.id.oldDatePrice1)
        wOldPrice1 = findViewById(R.id.oldWestPrice1)
        eOldPrice1 = findViewById(R.id.oldEastPrice1)
        date2 = findViewById(R.id.oldDatePrice2)
        wOldPrice2 = findViewById(R.id.oldWestPrice2)
        eOldPrice2 = findViewById(R.id.oldEastPrice2)
        date3 = findViewById(R.id.oldDatePrice3)
        wOldPrice3 = findViewById(R.id.oldWestPrice3)
        eOldPrice3 = findViewById(R.id.oldEastPrice3)
        date4 = findViewById(R.id.oldDatePrice4)
        wOldPrice4 = findViewById(R.id.oldWestPrice4)
        eOldPrice4 = findViewById(R.id.oldEastPrice4)

        sharedPreference = getSharedPreferences("savedPricesFlex", Context.MODE_PRIVATE)
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
        private val regex: Regex = """([0-9])\w+,[0-9]\w""".toRegex()
        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg params: Void): Void? {
            try {
                val document =  Jsoup.connect("https://norlys.dk/kundeservice/el/gaeldende-elpriser/").get()
                prices["priceW"] = regex.find(document.select(priceW).toString())!!.value + " øre/kWh"
                prices["priceE"] = regex.find(document.select(priceE).toString())!!.value + " øre/kWh"
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
            readData()
            textViewFlexW.text = prices["priceW"]
            textViewFlexE.text = prices["priceE"]
        }

        @SuppressLint("SimpleDateFormat")
        private fun saveData() {
            val prevData: String = sharedPreference.getString("flex", "")  ?: ""
            val dateFormat = SimpleDateFormat("dd/M/yyyy").format(Date()) + ':'
            if (!prevData.contains(dateFormat, ignoreCase = false)) {
                var result: String = prevData + dateFormat
                for (price in prices) {
                    result += "${price.value}:"
                }
                val editor: SharedPreferences.Editor = sharedPreference.edit()
                editor.apply {
                    putString("flex", result)
                }.apply()
            }
        }

        private fun readData() {
            val savedString: String = sharedPreference.getString("flex", "")  ?: ""
            if (savedString == "") saveData()
            val data: List<String> = savedString.split(':').dropLast(1)
            val textFields: MutableMap<String, String> = mutableMapOf(
                "date0" to "", "wOldPrice0" to "", "eOldPrice0" to "",
                "date1" to "", "wOldPrice1" to "", "eOldPrice1" to "",
                "date2" to "", "wOldPrice2" to "", "eOldPrice2" to "",
                "date3" to "", "wOldPrice3" to "", "eOldPrice3" to "",
                "date4" to "", "wOldPrice4" to "", "eOldPrice4" to "",
            )
            val _pricesMean: MutableList<Float> = mutableListOf()
            val pricesMean: MutableList<Float> = mutableListOf()
            val dates: MutableList<String> = mutableListOf()
            var counter = 0

            textFields.forEach { entry ->
                try {
                    textFields[entry.key] = data[counter]
                    if (data[counter].contains("øre/kWh")) {
                        _pricesMean.add(data[counter].replace(" øre/kWh", "").replace(',', '.').toFloat())
                    } else if (data[counter].contains(Regex("""([0-9])\w+\/+([0-9])+\/([0-9])+"""))) dates.add(data[counter])
                    ++counter
                } catch (e: IndexOutOfBoundsException) {
                    textFields[entry.key] = "Unset Value"
                }
            }
            for (i in 0 until _pricesMean.count() step 2) {
                pricesMean.add((_pricesMean[i] + _pricesMean[i+1]) / 2)
            }
            plotData(pricesMean, dates)

            date0.text = textFields["date0"]; wOldPrice0.text = textFields["wOldPrice0"]; eOldPrice0.text = textFields["eOldPrice0"]
            date1.text = textFields["date1"]; wOldPrice1.text = textFields["wOldPrice1"]; eOldPrice1.text = textFields["eOldPrice1"]
            date2.text = textFields["date2"]; wOldPrice2.text = textFields["wOldPrice2"]; eOldPrice2.text = textFields["eOldPrice2"]
            date3.text = textFields["date3"]; wOldPrice3.text = textFields["wOldPrice3"]; eOldPrice3.text = textFields["eOldPrice3"]
            date4.text = textFields["date4"]; wOldPrice4.text = textFields["wOldPrice4"]; eOldPrice4.text = textFields["eOldPrice4"]

        }
    }

    private fun extractData(data: List<String>) {
        data.forEach {t ->
            println(t)
        }
        /*val _pricesMean: MutableList<Float> = mutableListOf()
        val pricesMean: MutableList<Float> = mutableListOf()
        val dates: MutableList<String> = mutableListOf()
        var counter = 0
        textFields.forEach { entry ->
            try {
                textFields[entry.key] = data[counter]
                if (data[counter].contains("øre/kWh")) {
                    _pricesMean.add(data[counter].replace(" øre/kWh", "").replace(',', '.').toFloat())
                } else if (data[counter].contains(Regex("""([0-9])\w+\/+([0-9])+\/([0-9])+"""))) dates.add(data[counter])
                ++counter
            } catch (e: IndexOutOfBoundsException) {
                textFields[entry.key] = "Unset Value"
            }
        }
        for (i in 0 until _pricesMean.count() step 2) {
            pricesMean.add((_pricesMean[i] + _pricesMean[i+1]) / 2)
        }*/
    }

    private fun plotData(priceData: MutableList<Float>, priceDates: MutableList<String>) {
        val firstChartEntity = ChartEntity(Color.WHITE, priceData.toFloatArray())
        val legendArr = priceDates.toList()
        val list = ArrayList<ChartEntity>().apply {
            add(firstChartEntity)
        }
        val lineChart = findViewById<LineChart>(R.id.lineChart)
        lineChart.setLegend(legendArr)
        lineChart.setList(list)
    }
}

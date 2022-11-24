package eu.learning.checkelectricity

import org.jsoup.Jsoup
import android.os.Bundle
import java.io.IOException
import android.os.AsyncTask
import android.content.Intent
import android.widget.TextView
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import eu.learning.checkelectricity.databinding.ActivityFlexBinding
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class FlexActivity : AppCompatActivity(), SaveData {
    private lateinit var binding: ActivityFlexBinding
    private lateinit var textViewFlexW: TextView
    private lateinit var textViewFlexE: TextView
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
        private lateinit var flexW: String
        private lateinit var flexE: String
        private val regex: Regex = """([0-9])\w+,[0-9]\w""".toRegex()
        @Deprecated("Deprecated in Java")
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
        @Deprecated("Deprecated in Java")
        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            textViewFlexW.text = flexW
            textViewFlexE.text = flexE
        }
    }

    override var curTime: String
        get() = TODO("Not yet implemented")
        set(value) {}

    override var prices: Array<String>
        get() = TODO("Not yet implemented")
        set(value) {}
}


interface SaveData {
    var curTime: String
    var prices: Array<String>

    @SuppressLint("SimpleDateFormat")
    fun getDate(): String {
        val dateFormat = SimpleDateFormat("dd/M/yyyy")
        curTime = dateFormat.format(Date())
        return curTime
    }

    fun save() {
        TODO("Save data to internal file")
    }
}
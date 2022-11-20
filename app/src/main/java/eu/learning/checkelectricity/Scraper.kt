package eu.learning.checkelectricity

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import org.jsoup.Jsoup
import java.io.IOException

open class Scraper : AppCompatActivity(){
    private val regex: Regex = """([0-9])\w+,[0-9]\w""".toRegex() // <num><comma><num> 111,99
    private val poolElemW = "#elprodukter > div > div > div > div > table:nth-child(1) > tbody > tr:nth-child(2) > td:nth-child(2)"
    private val poolElemE = "#elprodukter > div > div > div > div > table:nth-child(1) > tbody > tr:nth-child(3) > td:nth-child(2)"
    private val flexElemW = "#elprodukter > div > div > div > div > table:nth-child(5) > tbody > tr:nth-child(2) > td:nth-child(2)"
    private val flexElemE = "#elprodukter > div > div > div > div > table:nth-child(5) > tbody > tr:nth-child(3) > td:nth-child(2)"
    private val combElemW = "#elprodukter > div > div > div > div > table:nth-child(3) > tbody > tr:nth-child(2) > td:nth-child(2)"
    private val combElemE = "#elprodukter > div > div > div > div > table:nth-child(3) > tbody > tr:nth-child(3) > td:nth-child(2)"

    fun scrape(): MutableMap<String, String> {
        val data: MutableMap<String, String> = mutableMapOf("poolW" to "", "poolE" to "", "flexW" to "", "flexE" to "", "combW" to "", "combE" to "")
        try {
            val document =  Jsoup.connect("https://norlys.dk/kundeservice/el/gaeldende-elpriser/").get()
            data["poolW"] = regex.find(document.select(poolElemW).toString())!!.value + " øre/kWh"
            data["poolE"] = regex.find(document.select(poolElemE).toString())!!.value + " øre/kWh"
            data["flexW"] = regex.find(document.select(flexElemW).toString())!!.value + " øre/kWh"
            data["flexE"] = regex.find(document.select(flexElemE).toString())!!.value + " øre/kWh"
            data["combW"] = regex.find(document.select(combElemW).toString())!!.value + " øre/kWh"
            data["combE"] = regex.find(document.select(combElemE).toString())!!.value + " øre/kWh"
        } catch (e: IOException) {
            data.forEach { entry ->
                data[entry.key] = e.stackTraceToString()
            }
        }
        return data
    }
}
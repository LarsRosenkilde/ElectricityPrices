package eu.learning.checkelectricity

import org.jsoup.Jsoup
import java.io.IOException

class DataHandler {
    private val regex: Regex = """([0-9])\w+,[0-9]\w""".toRegex() // <num><comma><num> 111,99
    private val priceFields: MutableMap<String, String> = mutableMapOf(
        "poolElemW" to "#elprodukter > div > div > div > div > table:nth-child(1) > tbody > tr:nth-child(2) > td:nth-child(2)",
        "poolElemE" to "#elprodukter > div > div > div > div > table:nth-child(1) > tbody > tr:nth-child(3) > td:nth-child(2)",
        "flexElemW" to "#elprodukter > div > div > div > div > table:nth-child(5) > tbody > tr:nth-child(2) > td:nth-child(2)",
        "flexElemE" to "#elprodukter > div > div > div > div > table:nth-child(5) > tbody > tr:nth-child(3) > td:nth-child(2)",
        "combElemW" to "#elprodukter > div > div > div > div > table:nth-child(3) > tbody > tr:nth-child(2) > td:nth-child(2)",
        "combElemE" to "#elprodukter > div > div > div > div > table:nth-child(3) > tbody > tr:nth-child(3) > td:nth-child(2)"
    )

    fun scrape(): MutableMap<String, String> {
        val data: MutableMap<String, String> = mutableMapOf(
            "poolW" to "",
            "poolE" to "",
            "flexW" to "",
            "flexE" to "",
            "combW" to "",
            "combE" to ""
        )
        try {
            val document =
                Jsoup.connect("https://norlys.dk/kundeservice/el/gaeldende-elpriser/").get()
            data["poolW"] = regex.find(
                document.select(priceFields["poolElemW"]).toString()
            )!!.value + " øre/kWh"
            data["poolE"] = regex.find(
                document.select(priceFields["poolElemE"]).toString()
            )!!.value + " øre/kWh"
            data["flexW"] = regex.find(
                document.select(priceFields["flexElemW"]).toString()
            )!!.value + " øre/kWh"
            data["flexE"] = regex.find(
                document.select(priceFields["flexElemE"]).toString()
            )!!.value + " øre/kWh"
            data["combW"] = regex.find(
                document.select(priceFields["combElemW"]).toString()
            )!!.value + " øre/kWh"
            data["combE"] = regex.find(
                document.select(priceFields["combElemE"]).toString()
            )!!.value + " øre/kWh"
        } catch (e: IOException) {
            data.forEach { entry ->
                data[entry.key] = e.stackTraceToString()
            }
        }
        return data
    }
}

fun main() {
    val data = DataHandler().scrape()

    println(data)
}
package eu.learning.checkelectricity

class DataScraper (data: String) {
    private val splitData: List<String> = data.dropLast(1).split(':')
    init {
        println("Data Amount: ${splitData.count()}")
    }

    fun divide (): Pair<MutableList<Float>, MutableList<String>> {
        val meanPrices: MutableList<Float> = mutableListOf()
        val dates: MutableList<String> = mutableListOf()
        for (i in 0 until splitData.count() step 3) {
            dates.add(splitData[i])
            meanPrices.add((splitData[i + 1]
                .replace(" øre/kWh", "")
                .replace(',', '.')
                .toFloat() + splitData[i + 2]
                .replace(" øre/kWh", "")
                .replace(',', '.')
                .toFloat()) / 2)
        }
        return Pair(meanPrices, dates)
    }
}

fun main() {
    val dummyData: String = "02/12/2022:231,12 øre/kWh:134,22 øre/kWh:01/12/2022:241,12 øre/kWh:154,22 øre/kWh:"
    val (prices, dates) = DataScraper(dummyData).divide()

    println(prices)
    println(dates)

    fun populateFields(dataString: String) {
        val data: List<String> = dataString.dropLast(1).split(':')
        val textFields: MutableMap<String, String> = mutableMapOf(
            "date0" to "", "wOldPrice0" to "", "eOldPrice0" to "",
            "date1" to "", "wOldPrice1" to "", "eOldPrice1" to "",
            "date2" to "", "wOldPrice2" to "", "eOldPrice2" to "",
            "date3" to "", "wOldPrice3" to "", "eOldPrice3" to "",
            "date4" to "", "wOldPrice4" to "", "eOldPrice4" to "",
        )
        var counter = 0
        textFields.forEach { elem ->
            try {
                textFields[elem.key] = data[counter++]
            } catch (E: IndexOutOfBoundsException) {
                textFields[elem.key] = "Unset Value"
            }
        }

        println(textFields)
    }

    populateFields(dummyData)
}
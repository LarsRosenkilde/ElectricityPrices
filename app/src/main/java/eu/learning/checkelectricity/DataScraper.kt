package eu.learning.checkelectricity

class DataScraper (data: String) {
    private val splitData: List<String> = data.dropLast(1).split(':')
    init {
        println("Data Amount: ${splitData.count()}")
        val (prices, dates) = divide()

        println(prices)
        println(dates)
    }

    private fun divide (): Pair<MutableList<Float>, MutableList<String>> {
        val meanPrices: MutableList<Float> = mutableListOf()
        val dates: MutableList<String> = mutableListOf()
        for (i in 0 until splitData.count() step 3) {
            meanPrices.add((splitData[i]
                .replace(" øre/kWh", "")
                .replace(',', '.')
                .toFloat() + splitData[i + 1]
                .replace(" øre/kWh", "")
                .replace(',', '.')
                .toFloat()) / 2)
            dates.add(splitData[i + 2])
        }
        return Pair(meanPrices, dates)
    }

}

fun main() {
    val dummyData: String = "723,82 øre/kWh:733,82 øre/kWh:01/12/2022:523,82 øre/kWh:533,82 øre/kWh:02/12/2022:"
    DataScraper(dummyData)

}

/*
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
*/
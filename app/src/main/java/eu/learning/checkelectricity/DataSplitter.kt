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
    val dummyData: String = "231,12 øre/kWh:134,22 øre/kWh:01/12/2022:241,12 øre/kWh:154,22 øre/kWh:02/12/2022:"
    val (prices, dates) = DataScraper(dummyData).divide()

    println(prices)
    println(dates)
}
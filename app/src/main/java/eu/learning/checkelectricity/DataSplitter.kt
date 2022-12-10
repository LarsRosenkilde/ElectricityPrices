package eu.learning.checkelectricity

class DataScraper(data: String) {
    private val splitData: List<String> = data.dropLast(1).split(':')

    fun divide(): Pair<List<String>, FloatArray> {
        val meanPrices: MutableList<Float> = mutableListOf()
        val dates: MutableList<String> = mutableListOf()
        for (i in 0 until splitData.count() step 3) {
            try {
                dates.add(splitData[i])
                meanPrices.add((splitData[i + 1]
                    .replace(" øre/kWh", "")
                    .replace(',', '.')
                    .toFloat() + splitData[i + 2]
                    .replace(" øre/kWh", "")
                    .replace(',', '.')
                    .toFloat()) / 2)
            } catch(e: NumberFormatException) {
                print(e.printStackTrace())
                dates.add(e.stackTraceToString())
                meanPrices.add(1000F)
            }
        }
        return Pair(dates.toList(), meanPrices.toFloatArray())
    }
}
/*
fun main() {
    /*
    val dummyData: String = "02/12/2022:231,12 øre/kWh:134,22 øre/kWh:01/12/2022:241,12 øre/kWh:154,22 øre/kWh:"
    val (prices, dates) = DataScraper(dummyData).divide()

    println(prices)
    println(dates)
    */
    val savedString = DataScraper::class.java.getResource("DebugData.txt")?.readText()

    print(savedString)
}

 */
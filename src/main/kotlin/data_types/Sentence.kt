package data_types

data class Sentence(
    val words: List<Word>,
) {
    override fun toString(): String
    {
        return toStrings().joinToString(" ")
    }


    fun toStrings(): List<String>
    {
        return words.map { word -> word.toString() }
    }
}
package data_types

data class Line(
    val sentences: List<Sentence>
) {
    override fun toString(): String
    {
        return toStrings().joinToString(" ")
    }

    fun toStrings(): List<String>
    {
        return this.sentences.map { line -> line.toString() }
    }
}
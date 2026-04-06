package data_types

data class Fragment(
    val lines: List<Line>,
) {
    override fun toString(): String
    {
        return toStrings().joinToString("\n")
    }


    fun toStrings(): List<String>
    {
        return lines.map { line -> line.toString() }
    }
}

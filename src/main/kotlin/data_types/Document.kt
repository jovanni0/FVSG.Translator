package data_types

data class Document(
    val fragments: List<Fragment>
) {
    override fun toString(): String
    {
        return toStrings().joinToString("\n\n")
    }


    fun toStrings(): List<String>
    {
        return fragments.map { fragment -> fragment.toString() }
    }
}
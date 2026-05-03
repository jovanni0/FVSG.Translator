import data_types.Word
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import processors.DocumentSplitter
import processors.NameHarvester
import kotlin.test.Test



class NameHarvesterTest
{
    private val harvester = NameHarvester()
    private val doc_splitter = DocumentSplitter()

    /**
     * names in the middle of a sentence (low ambiguity) should be harvested.
     */
    @Test
    fun testMidSentenceName()
    {
        val tokens = listOf(
            Word("", "He", "", is_sentence_start = true),
            Word("", "saw", ""),
            Word("", "John", "")
        )

        val names = harvester.run(tokens)

        assertTrue(names.contains("John"))
        assertFalse(names.contains("He"))
    }

    @Test
    fun `test name in the middle of the sentence`()
    {
        val input = "He saw John."

        val names = harvester.run(doc_splitter.splitLine(input))

        assertTrue(names.contains("John"))
        assertFalse(names.contains("He"))
    }


//    /**
//     * if a word is capitalized at the start of a sentence BUT also appears
//     * in the middle of a sentence elsewhere, it should be confirmed as a name.
//     */
//    @Test
//    fun testStartOfSentenceNameCrossReference()
//    {
//        val tokens = listOf(
//            Word("", "Alice", "", is_sentence_start = true),
//            Word("", "saw", ""),
//            Word("", "Bob", ""),
//            ParagraphBreak,
//            Word("", "Then", "", is_sentence_start = true),
//            Word("", "Alice", ""),
//            Word("", "left", "")
//        )
//
//        val names = harvester.run(tokens)
//
//        assertTrue(names.contains("Alice"))
//        assertTrue(names.contains("Bob"))
//        assertFalse(names.contains("Then"))
//    }


    /**
     * We should eventually exclude common titles that aren't actually names.
     */
    @Test
    fun testExcludeCommonTitles()
    {
        val tokens = listOf(
            Word("", "Mr.", "", is_sentence_start = true),
            Word("", "Smith", "", is_sentence_start = true)
        )

        val names = harvester.run(tokens)

        assertFalse(names.contains("Smith"))
        assertFalse(names.contains("Mr."))
    }

    @Test
    fun `test the exclusion of common titles`()
    {
        val input = "Ok Mr. Smith."

        val names = harvester.run(doc_splitter.splitLine(input))

        assertFalse(names.contains("Smith"))
        assertFalse(names.contains("Mr."))
    }


    /**
     * make sure that words that contain uppercase letters are also correctly selected as names, even if the first
     * letter is not uppercase.
     */
    @Test
    fun testIncludeWordsWithUppercaseLetters()
    {
        val tokens = listOf(
            Word("", "The", "", is_sentence_start = true),
            Word("", "sRs", "")
        )

        val names = harvester.run(tokens)

        assertTrue(names.contains("sRs"))
    }

    @Test
    fun `test names that do not start with an uppercase letter`()
    {
        val input = "The sRs is a beast."

        val names = harvester.run(doc_splitter.splitLine(input))

        assertTrue(names.contains("sRs"))
    }


    /**
     * make sure that name-case words that contain numbers are also correctly selected as names.
     */
    @Test
    fun testNameWithNumber()
    {
        val tokens = listOf(
            Word("", "The", "", is_sentence_start = true),
            Word("", "S0m", "")
        )

        val names = harvester.run(tokens)

        assertTrue(names.contains("S0m"))
    }

    @Test
    fun `test name that includes a number`()
    {
        val input = "The S0m rises!"

        val names = harvester.run(doc_splitter.splitLine(input))

        assertTrue(names.contains("S0m"))
    }


    /**
     * words that are never names (except in combination with other words) are not included in the names list.
     */
    @Test
    fun testSomeWordsAreNeverNames()
    {
        val tokens = listOf(
            Word("", "Flight", "", is_sentence_start = true),
            Word("", "Of", ""),
            Word("", "The", ""),
            Word("", "Special", "")
        )

        val names = harvester.run(tokens)

        assertFalse(names.contains("Of"))
        assertFalse(names.contains("The"))
    }

    @Test
    fun `test words that are in the banned list of names`()
    {
        val input = "Flight Of The Special."

        val names = harvester.run(doc_splitter.splitLine(input))

        assertFalse(names.contains("Of"))
        assertFalse(names.contains("The"))
    }

    @Test
    fun `test line that starts with #`()
    {
        val input = "# Chapter One"

        val names = harvester.run(doc_splitter.splitLine(input))

        assertFalse(names.contains("Chapter"))
        assertFalse(names.contains("One"))
    }


    @Test
    fun `first line of doc`()
    {
        val input = "For Cath and Alexander"

        val names = harvester.run(doc_splitter.splitDoc(input))

        assertTrue(names.contains("Cath"))
        assertTrue(names.contains("Alexander"))
    }

        @Test
        fun `ignore namecase word at speech start`()
        {
            val input = "screeched “You”"

            val sentences = doc_splitter.splitLine(input)
            val names = harvester.run(sentences)

            assertEquals(0, names.size)
        }
}
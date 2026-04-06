import processors.CaseProcessor
import processors.DocumentSplitter
import kotlin.test.Test
import kotlin.test.assertEquals

class CaseProcessorTest
{
    private val doc_splitter = DocumentSplitter()
    private val case_processor = CaseProcessor()


    @Test
    fun `simple sentence with no names`()
    {
        val input = "This is a sentence."
        val sentence = doc_splitter.splitLine(input).sentences[0]

        val result = case_processor.run(sentence)

        assertEquals("this is a sentence.", result.toString())
    }


    @Test
    fun `simple sentence with names`()
    {
        val input = "I am here."
        val sentence = doc_splitter.splitLine(input).sentences[0]
        case_processor.setNames(setOf("I"))

        val result = case_processor.run(sentence)

        assertEquals("I am here.", result.toString())
    }


    @Test
    fun `names with styling`()
    {
        val input = "*Gigolo Aunt*’s"
        val sentence = doc_splitter.splitLine(input).sentences[0]
        case_processor.setNames(setOf("Gigolo", "Aunt’s"))

        val result = case_processor.run(sentence)

        assertEquals(input, result.toString())
    }


    @Test
    fun `add name possessive from to the name set`()
    {
        val input = setOf("John")

        case_processor.setNames(input)

        assertEquals(setOf("John", "John’s"), case_processor.names)
    }


    @Test
    fun `add name possessive for s-terminated names`()
    {
        val input = setOf("Cas")

        case_processor.setNames(input)

        assertEquals(setOf("Cas", "Cas’"), case_processor.names)
    }


    @Test
    fun `skip names that are in the possessive form`()
    {
        val input = setOf("John’s")

        case_processor.setNames(input)

        assertEquals(setOf("John’s"), case_processor.names)
    }


    @Test
    fun `skip names that are s-terminated and in the possessive form`()
    {
        val input = setOf("Cas’")

        case_processor.setNames(input)

        assertEquals(setOf("Cas’"), case_processor.names)
    }
}
import org.junit.jupiter.api.Nested
import processors.DocumentSplitter
import processors.PunctuationProcessor
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PunctuationProcessorTest
{
    val punctuation_processor = PunctuationProcessor()
    val doc_splitter = DocumentSplitter()


    @Test
    fun `insert period on sentence with one speech block`()
    {
        val input = "“Are you getting something?”"

        val result = punctuation_processor.run(doc_splitter.splitLine(input).sentences[0])

        assertEquals("“Are you getting something?.”", result.toString())
    }


    @Test
    fun `insert period on sentence with multiple interlaced speech blocks`()
    {
        val input = "“Hey,” she said, “What are you doing?”"

        val result = punctuation_processor.run(doc_splitter.splitLine(input).sentences[0])

        assertTrue(result.toString().endsWith("?”."))
    }


    @Test
    fun `insert period on sentence with multiple constant speech blocks`()
    {
        val input = "“It's done. I have done it!”"

        val result = punctuation_processor.run(doc_splitter.splitLine(input).sentences[1])

        assertEquals("I have done it!.”", result.toString())
    }


    @Test
    fun `shift comma on sentence with multiple interlaced speech blocks`()
    {
        val input = "“Hey,” she said, “What are you doing?”"

        val result = punctuation_processor.run(doc_splitter.splitLine(input).sentences[0])

        assertTrue(result.toString().startsWith("“Hey”,"))
    }


    @Test
    fun `shift period on sentence with multiple interlaced speech blocks`()
    {
        val input = "“Hey,” she said, “here.”"

        val result = punctuation_processor.run(doc_splitter.splitLine(input).sentences[0])

        assertTrue(result.toString().endsWith("“here”."))
    }


    @Test
    fun `don't shift period on single sentence line`()
    {
        val input = "“here.”"

        val result = punctuation_processor.run(doc_splitter.splitLine(input).sentences[0])

        assertEquals("“here.”", result.toString())
    }


    @Test
    fun `line`()
    {
        val input = doc_splitter.splitLine("“We going?” Spider asked.")

        val result = punctuation_processor.run(input)

        assertEquals("“We going?”, Spider asked.", result.toString())
    }


    @Test
    fun `ignore scene break line`()
    {
        val input = doc_splitter.splitLine("---")

        val result = punctuation_processor.run(input)

        assertEquals("---", result.toString())
    }


    @Nested
    inner class InserPeriod
    {
        @Test
        fun `insert period after ellipses at sentence end`()
        {
            val input = "Afterward… Forget it."
            val output = "Afterward…. Forget it."

            val split = doc_splitter.splitLine(input)
            val result = punctuation_processor.run(split)

            assertEquals(output, result.toString())
        }
    }


    @Nested
    inner class InsertComma
    {
        @Test
        fun `insert comma after question when detecting 'called'`()
        {
            val input = "“Going?” LionWalker called over the wind."
            val output = "“Going?”, LionWalker called over the wind."

            val split = doc_splitter.splitLine(input)
            val result = punctuation_processor.run(split)

            assertEquals(output, result.toString())
        }

        @Test
        fun `insert comma after question when detecting 'asking'`()
        {
            val input = "“Don?” Paula was asking."
            val output = "“Don?”, Paula was asking."

            val split = doc_splitter.splitLine(input)
            val result = punctuation_processor.run(split)

            assertEquals(output, result.toString())
        }

        @Test
        fun `don't insert comma after ellipsis`()
        {
            val input = "I'm… done."
            val output = input

            val split = doc_splitter.splitLine(input)
            val result = punctuation_processor.run(split)

            assertEquals(output, result.toString())
        }
    }
}
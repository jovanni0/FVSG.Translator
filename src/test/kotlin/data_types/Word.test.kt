package data_types

import kotlin.test.Test
import kotlin.test.assertEquals


class WordTest 
{
    @Test
    fun testComplexTails()
    {
        val complex = wordFromString("bad!.”")

        assertEquals("!", complex.tail.first().toString())
        assertEquals(".", complex.tail[1].toString())
        assertEquals("”", complex.tail.last().toString())
    }


    @Test
    fun testContractions()
    {
        val result = wordFromString("can't,")

        assertEquals("can't", result.body)
        assertEquals(",", result.tail)
    }


    @Test
    fun testNumbers()
    {
        val result = wordFromString("10.5...")

        assertEquals("10.5", result.body)
        assertEquals("...", result.tail)
    }


    @Test
    fun testStandaloneMarkers()
    {
        val result = wordFromString("--")

        assertEquals("--", result.head)
        assertEquals("", result.body)
    }


    @Test
    fun testHeadPrecedence()
    {
        val result = wordFromString("***")

        assertEquals("***", result.head, "Should be all head due to precedence")
        assertEquals("", result.body)
        assertEquals("", result.tail)
    }


    @Test
    fun testMarkdownFormatting()
    {
        val result = wordFromString("*“word”*")

        assertEquals("*“", result.head)
        assertEquals("word", result.body)
        assertEquals("”*", result.tail)
    }


    @Test
    fun testEmphasisOnName()
    {
        val result = wordFromString("*Rose*")

        assertEquals("Rose", result.body, "Body should be clean for name detection")
    }


    @Test
    fun testHeadPrecedenceWithMixedSymbols()
    {
        val result = wordFromString("*-*")

        assertEquals("*-*", result.head)
        assertEquals("", result.body)
        assertEquals("", result.tail)
    }


    @Test
    fun testHyphenatedWords()
    {
        val result = wordFromString("mother-in-law.")

        assertEquals("", result.head)
        assertEquals("mother-in-law", result.body)
        assertEquals(".", result.tail)
    }


    @Test
    fun testPossessivesWithQuotes()
    {
        val result = wordFromString("“Rose's”")

        assertEquals("“", result.head)
        assertEquals("Rose's", result.body)
        assertEquals("”", result.tail)
    }


    @Test
    fun testAcronymAtSentenceEnd()
    {
        val result = wordFromString("U.S.A.")

        assertEquals("U.S.A", result.body)
        assertEquals(".", result.tail)
    }


    @Test
    fun testNumericalDecimals()
    {
        val result = wordFromString("3.14!!")

        assertEquals("3.14", result.body)
        assertEquals("!!", result.tail)
    }


    @Test
    fun testEmptyBodyWithFormatting()
    {
        val result = wordFromString("*“...!”*")

        assertEquals("*“", result.head)
        assertEquals("", result.body)
        assertEquals("...!”*", result.tail)
    }


    @Test
    fun testUrlIntegrity()
    {
        val result = wordFromString("https://google.com")

        assertEquals("https://google.com", result.body)
    }


    @Test
    fun testWordInStraightDoubleQuotes()
    {
        val result = wordFromString("\"word\"")

        assertEquals(result, Word("\"", "word", "\""))
    }


    @Test
    fun testWordInStraightSingleQuotes()
    {
        val result = wordFromString("'word'")

        assertEquals(result, Word("'", "word", "'"))
    }


    @Test
    fun `insert period on naked word with before true`()
    {
        val input = wordFromString("word")

        input.insertPeriod(true)

        assertEquals("word.", input.toString())
    }


//    @Test
//    fun `unwrap word`()
//    {
//        val result = wordFromString("-- ”")
//
//        assertEquals("--", result.head)
//    }


    @Test
    fun `insert period on naked word with before false`()
    {
        val input = wordFromString("word")

        input.insertPeriod(false)

        assertEquals("word.", input.toString())
    }


    @Test
    fun `insert period on word already having with before true`()
    {
        val input = wordFromString("word.")

        input.insertPeriod(true)

        assertEquals("word.", input.toString())
    }


    @Test
    fun `insert period on word already having with before false`()
    {
        val input = wordFromString("word.")

        input.insertPeriod(false)

        assertEquals("word.", input.toString())
    }


    @Test
    fun `insert period on sentence end word with before true`()
    {
        val input = wordFromString("word”")

        input.insertPeriod(true)

        assertEquals("word.”", input.toString())
    }


    @Test
    fun `insert period on sentence end word with before false`()
    {
        val input = wordFromString("word”")

        input.insertPeriod(false)

        assertEquals("word”.", input.toString())
    }


    @Test
    fun `insert period on sentence start word with before true`()
    {
        val input = wordFromString("“word")

        input.insertPeriod(true)

        assertEquals("“word.", input.toString())
    }


    @Test
    fun `insert period on sentence start word with before false`()
    {
        val input = wordFromString("“word")

        input.insertPeriod(true)

        assertEquals("“word.", input.toString())
    }

    @Test
    fun `insert period on word with question mark with before true`()
    {
        val input = wordFromString("word?")

        input.insertPeriod(true)

        assertEquals("word?.", input.toString())
    }


    @Test
    fun `insert period on word with question mark with before false`()
    {
        val input = wordFromString("word?")

        input.insertPeriod(false)

        assertEquals("word?.", input.toString())
    }


    @Test
    fun `insert period on word with question mark and sentence end with before true`()
    {
        val input = wordFromString("word?”")

        input.insertPeriod(true)

        assertEquals("word?.”", input.toString())
    }


    @Test
    fun `insert period on word with question mark and sentence end with before false`()
    {
        val input = wordFromString("word?”")

        input.insertPeriod(false)

        assertEquals("word?”.", input.toString())
    }


    @Test
    fun `insert period on word with exclamation mark`()
    {
        val input = wordFromString("word!")

        input.insertPeriod(false)

        assertEquals("word!.", input.toString())
    }


    @Test
    fun `insert period on word with ellipses mark`()
    {
        val input = wordFromString("word…")

        input.insertPeriod(false)

        assertEquals("word….", input.toString())
    }


    @Test
    fun `shift period`()
    {
        val input = wordFromString("come.”")

        input.shiftPeriodAfterEndQuotes()

        assertEquals("come”.", input.toString())
    }
}
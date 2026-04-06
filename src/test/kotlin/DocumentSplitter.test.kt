import processors.DocumentSplitter
import kotlin.test.Test
import kotlin.test.assertEquals

class DocumentSplitterTest
{
    val doc_splitter = DocumentSplitter()


    @Test
    fun `test line splitter for simple line`()
    {
        val input = "Line 1. And Line 2. 3."

        val result = doc_splitter.splitLine(input)

        assertEquals(listOf("Line 1.", "And Line 2.", "3."), result.toStrings())
    }


    @Test
    fun `test line splitter with multi-sentence speech`()
    {
        val input = "“Line 1. And Line 2. 3.”"

        val result = doc_splitter.splitLine(input)

        assertEquals(listOf("“Line 1.", "And Line 2.", "3.”"), result.toStrings())
    }


    @Test
    fun `test line splitter with single speech for half sentence`()
    {
        val input = "“Line 1,” I said."

        val result = doc_splitter.splitLine(input)

        assertEquals(listOf("“Line 1,” I said."), result.toStrings())
    }


    @Test
    fun `test line splitter with double speech in sentence`()
    {
        val input = "“Line 1,” I said, “will work.”"

        val result = doc_splitter.splitLine(input)

        assertEquals(listOf("“Line 1,” I said, “will work.”"), result.toStrings())
    }


    @Test
    fun `test line splitter with multiple sentences in speech`()
    {
        val input = "“Line 1. I will do it.”"

        val result = doc_splitter.splitLine(input)

        assertEquals(listOf("“Line 1.", "I will do it.”"), result.toStrings())
    }



    @Test
    fun `fragment splitter splits lines by newline`()
    {
        val fragment = "Line one\nLine two\nLine three"

        val result = doc_splitter.splitFragment(fragment)

        assertEquals(3, result.size)
    }

    @Test
    fun `splitFragment handles windows newlines`()
    {
        val fragment = "Line one\r\nLine two\r\nLine three"

        val result = doc_splitter.splitFragment(fragment)

        assertEquals(3, result.size)
    }

    @Test
    fun `splitFragment returns single line if no newline`() {
        val fragment = "Only one line"

        val result = doc_splitter.splitFragment(fragment)

        assertEquals(1, result.size)
    }

    @Test
    fun `splitDoc splits fragments separated by empty lines`() {
        val doc = """
            First fragment line 1
            First fragment line 2
            
            Second fragment line 1
            Second fragment line 2
        """.trimIndent()

        val result = doc_splitter.splitDoc(doc)

        assertEquals(2, result.fragments.size)
    }

    @Test
    fun `splitDoc handles multiple blank lines between fragments`() {
        val doc = "Fragment one\n\n\nFragment two"

        val result = doc_splitter.splitDoc(doc)

        assertEquals(2, result.fragments.size)
    }

    @Test
    fun `splitDoc returns single fragment when no blank lines exist`() {
        val doc = "Line one\nLine two"

        val result = doc_splitter.splitDoc(doc)

        assertEquals(1, result.fragments.size)
    }
}
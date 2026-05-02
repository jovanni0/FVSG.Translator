import org.junit.jupiter.api.Nested
import processors.PreProcessor
import kotlin.test.Test
import kotlin.test.assertEquals



class PreProcessorTest
{
    private val preprocessor = PreProcessor()


    @Nested
    inner class Run
    {
        @Test
        fun `type 1A spread ellipsis condensation`()
        {
            val value = "strokes . . . ”"
            val expected = "strokes…”"

            val result = preprocessor.run(value)

            assertEquals(result, expected)
        }


        @Test
        fun `type 1B spread ellipsis condensation`()
        {
            val value = "strokes . . .”"
            val expected = "strokes…”"

            val result = preprocessor.run(value)

            assertEquals(result, expected)
        }


        @Test
        fun `type 2A spread ellipsis condensation`()
        {
            val value = "“ . . . we're"
            val expected = "“… we're"

            val result = preprocessor.run(value)

            assertEquals(result, expected)
        }


        @Test
        fun `type 2B spread ellipsis condensation`()
        {
            val value = "“. . . we're"
            val expected = "“… we're"

            val result = preprocessor.run(value)

            assertEquals(result, expected)
        }


        @Test
        fun `type 3A spread ellipsis condensation`()
        {
            val value = "text . . . "
            val expected = "text… "

            val result = preprocessor.run(value)

            assertEquals(result, expected)
        }


        @Test
        fun `type 3B spread ellipsis condensation`()
        {
            val value = "text. . . "
            val expected = "text… "

            val result = preprocessor.run(value)

            assertEquals(result, expected)
        }
    }
}

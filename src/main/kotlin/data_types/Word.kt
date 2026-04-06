package data_types

data class Word(
    val head: String,
    val body: String,
    var tail: String,
    var is_sentence_start: Boolean = false,
    var required_stabilizer: Char? = null
) {
    override fun toString(): String
    {
        return this.head + this.body + this.tail
    }


    fun isNameCase(): Boolean
    {
        return body.isNotEmpty() && body.any { it.isUpperCase() }
    }


    fun bodyWithoutMarkers(): String
    {
        return body.replace("*", "")
    }


    fun insertPeriod(before: Boolean = true)
    {
        val sentence_end_punctuation = setOf('?', '!', '…')

        if (tail.contains('.'))
        {
            return
        }

        if (tail.contains('”'))
        {
            if (before)
            {
                tail = tail.replace("”", ".”")
            }
            else
            {
                tail = tail.replace("”", "”.")
            }
        }
        else if (tail.any { it in sentence_end_punctuation })
        {
            for (index in tail.indices.reversed())
            {
                val char = tail[index]

                if (char in sentence_end_punctuation)
                {
                    tail = tail.insert(index + 1, '.')
                    break
                }
            }
        }
        else
        {
            tail += '.'
        }
    }


    fun insertComma()
    {
        tail += ','
    }


    fun isSceneBreak(): Boolean
    {
        return this.toString() == "---"
    }


    fun shiftPeriodAfterEndQuotes()
    {
        val index_quotes = tail.indexOf('”')
        val index_last_period = tail.indexOfLast { it == '.' }

        if (index_quotes < 0 || index_last_period < 0 || index_last_period > index_quotes)
        {
            return
        }

        tail = tail
            .insert(index_quotes + 1, '.')
            .removeRange(index_last_period, index_last_period + 1)
    }
}

private val headChars: Set<Char> = setOf('“', '‘', '-', '*', '"', '\'')
private val tailChars: Set<Char> = setOf('?', '!', '.', ',', ':', ';', '”', '’', '…', '-', '*', '"', '\'')


fun wordFromString(word: String): Word
{
    val head = unwrapHead(word)
    val without_head = word.substring(head.length)

    val tail = unwrapTail(without_head)

    val body = without_head.substring(0, without_head.length - tail.length)

    return Word(head = head, body = body, tail = tail)
}


private fun unwrapHead(word: String): String
{
    return word.takeWhile { it in headChars }
}


private fun unwrapTail(word: String): String
{
    return word.takeLastWhile { it in tailChars }
}

fun String.insert(index: Int, char: Char): String
{
    return this.substring(0, index) + char + this.substring(index)
}
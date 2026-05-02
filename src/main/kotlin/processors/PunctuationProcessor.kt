package processors

import data_types.Document
import data_types.Fragment
import data_types.Line
import data_types.Sentence



class PunctuationProcessor
{
    fun run(doc: Document): Document
    {
        val new_fragments = doc.fragments.map { fragment ->
            this.run(fragment)
        }

        return Document(new_fragments)
    }


    fun run(fragment: Fragment): Fragment
    {
        val new_lines = fragment.lines.map { line ->
            this.run(line)
        }

        return Fragment(new_lines)
    }


    fun run(line: Line): Line
    {
        val new_sentences = mutableListOf<Sentence>()

        if (line.sentences.size == 1 && line.sentences.first().words.size == 1 && line.sentences.first().words.first().isSceneBreak())
        {
            return Line(line.sentences)
        }

        for (index in line.sentences.indices)
        {
            val sentence = line.sentences[index]

            if (index < line.sentences.size - 1)
            {
                val next_sentence = line.sentences[index + 1]

                val sentence_ending_hint = isCurrentSentenceEnding(next_sentence)

                new_sentences.add(this.run(sentence, sentence_ending_hint))
            }
            else
            {
                new_sentences.add(this.run(sentence))
            }
        }

//        val new_sentences = line.sentences.map { sentence ->
//            this.run(sentence)
//        }

        return Line(new_sentences)
    }


    fun run(sentence: Sentence, is_ending_hint: Boolean = true): Sentence
    {
        var speech_counter = 0

        sentence.words.forEachIndexed { index, word ->
            if (word.head.contains('“'))
            {
                speech_counter++
            }

            if (index == sentence.words.lastIndex && !word.tail.contains('.'))
            {
                if (!is_ending_hint)
                {
                    word.insertComma()
                }
                else if (speech_counter <= 1)
                {
                    word.insertPeriod(true)
                }
                else
                {
                    word.insertPeriod(false)
                }
            }
            else if (word.tail.contains('.') && word.tail.contains('”') && speech_counter > 1)
            {
                word.shiftPeriodAfterEndQuotes()
            }
            else if (word.tail.contains(",”"))
            {
                word.tail = word.tail.replace(",”", "”,")
            }
        }

        return sentence
    }


    fun isCurrentSentenceEnding(next_sentence: Sentence): Boolean
    {
        val special_words = setOf(
            "said",
            "asked",
            "yelled",
            "called",
            "asking"
        )

        val words = next_sentence.words

        if (words.size < 1)
        {
            return true
        }

        if (!words[0].isNameCase())
        {
            return false
        }

        val vision_end_index = (3).coerceAtMost(words.size)
        val in_vision_range = words.subList(0, vision_end_index)
        if (in_vision_range.any { it.body in special_words })
        {
            return false
        }

        return true
    }
}
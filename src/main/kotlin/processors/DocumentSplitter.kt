package processors

import data_types.Document
import data_types.Fragment
import data_types.Line
import data_types.Sentence
import data_types.Word
import data_types.wordFromString

class DocumentSplitter
{
//    fun splitDoc(doc: String): Document
//    {
//        val raw_fragments = doc.split(Regex("(\\r?\\n\\s*){2,}"))
//        val fragments = mutableListOf<Fragment>()
//
//        for (fragment in raw_fragments)
//        {
//            val lines = splitFragment(fragment)
//            fragments.add(Fragment(lines))
//        }
//
//        return Document(fragments)
//    }
//
//
//    fun splitFragment(fragment: String): List<Line>
//    {
//        val raw_lines = fragment.split(Regex("\\r?\\n"))
//        val lines = mutableListOf<Line>()
//
//        for (line in raw_lines)
//        {
//            val sentences = splitLine(line)
//            lines.add(sentences)
//        }
//
//        return lines
//    }


    val end_of_sentence = setOf(
        '.',
        '?',
        '!',
        '…'
    )


    fun splitDoc(doc: String): Document
    {
        val fragments = doc
            .split(Regex("(\\r?\\n\\s*){2,}"))
            .map { fragment -> Fragment(splitFragment(fragment)) }

        return Document(fragments)
    }

    fun splitFragment(fragment: String): List<Line>
    {
        return fragment
            .split(Regex("\\r?\\n"))
            .map { line -> splitLine(line) }
    }


    fun splitLine(line: String): Line
    {
        val sentences = mutableListOf<Sentence>()
        var current_sentence = mutableListOf<Word>()

        for (word in line.splitIntoWords())
        {
            current_sentence.add(wordFromString(word))

            if (word.isSentenceEnd())
            {
                sentences += Sentence(current_sentence.toList())
                current_sentence = mutableListOf()
            }
        }

        if (current_sentence.isNotEmpty())
        {
            sentences.add(Sentence(current_sentence.toList()))
        }

        return Line(sentences)
    }


    fun String.isSentenceEnd(): Boolean
    {
        return this.any { it in end_of_sentence }
    }


    fun String.splitIntoWords(): List<String>
    {
        return this.split(Regex("\\s+")).filter { it.isNotEmpty() }
    }
}
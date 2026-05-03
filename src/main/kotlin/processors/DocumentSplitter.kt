package processors

import data_types.Document
import data_types.Fragment
import data_types.Line
import data_types.Sentence
import data_types.Word
import data_types.wordFromString


/**
 * splits the document into its components:
 * - fragment
 * - line
 * - sentence
 *
 * each component is defined by a character/sequesnce which is used as the boundary indicator.
 */
class DocumentSplitter
{
    val fragment_boundary = Regex("(\\r?\\n\\s*){2,}")
    val line_boundary = Regex("\\r?\\n")
    val word_boundary = Regex("\\s+")

    /**
     * the list of characters considered markers of sentence end.
     *
     * this list contains indicators of possible sentence end. because the process is aggressive, every character that
     * can be used to separate sentences will be used to split the line into sentences. this way a namecase word will
     * not be confused with a name because it's the start of a sentence.
     */
    val end_of_sentence = setOf(
        '.',
        '?',
        '!',
        '…',
        ':'
    )


    /**
     * split a document into it's components (fragments, lines, sentences).
     */
    fun splitDoc(doc: String): Document
    {
        val fragments = doc
            .split(fragment_boundary)
            .map { fragment -> Fragment(splitFragment(fragment)) }

        return Document(fragments)
    }


    /**
     * split a fragment into lines.
     */
    fun splitFragment(fragment: String): List<Line>
    {
        return fragment
            .split(line_boundary)
            .map { line -> splitLine(line) }
    }


    /**
     * split a line into sentences.
     */
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


    /**
     * check if the string contains any characters defined in `end_of_sentence`.
     * return `True` if contains, else `False`.
     */
    fun String.isSentenceEnd(): Boolean
    {
        return this.any { it in end_of_sentence }
    }


    /**
     * split a string into words, discarding whitespaces and empty strings.
     * the splitting is done based on `word_boundary` Regex.
     */
    fun String.splitIntoWords(): List<String>
    {
        return this.split(word_boundary).filter { it.isNotEmpty() }
    }
}
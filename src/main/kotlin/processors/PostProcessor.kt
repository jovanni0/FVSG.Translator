package processors

import data_types.Document
import data_types.Fragment
import data_types.Line
import data_types.Sentence

class PostProcessor
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
        val sentences = line.sentences.map { sentence ->
            this.run(sentence)
        }

        return Line(sentences)
    }


    fun run(sentence: Sentence): Sentence
    {
        val words = sentence.words.map { word ->
            word.copy(body = word.body.replace('’', '\''))
        }

        return Sentence(words)
    }
}
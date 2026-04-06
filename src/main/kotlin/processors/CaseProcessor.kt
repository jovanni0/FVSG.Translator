package processors

import data_types.Document
import data_types.Fragment
import data_types.Line
import data_types.Sentence


class CaseProcessor
{
    var names = setOf<String>()
        private set

    private val always_names = setOf("I", "I’ve", "I’d", "I’m", "I’ll")


    fun setNames(names: Set<String>)
    {
        this.names = addPossesives(names)
    }

    fun addPossesives(names: Set<String>): Set<String>
    {
        val complete_names = mutableSetOf<String>()

        names.forEach { name ->
            complete_names.add(name)

            if (!name.endsWith('’') && !name.endsWith("’s"))
            {
                var possesive = name + '’'
                if (!name.endsWith('s'))
                {
                    possesive += 's'
                }

                complete_names.add(possesive)
            }
        }

        return complete_names
    }

    fun run(doc: Document): Document
    {
        val fragments = doc.fragments.map { fragment ->
            this.run(fragment)
        }

        return Document(fragments)
    }


    fun run(fragment: Fragment): Fragment
    {
        val lines = fragment.lines.map { line ->
            this.run(line)
        }

        return Fragment(lines)
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
            if (word.bodyWithoutMarkers() in names ||
                word.bodyWithoutMarkers() in always_names )
            {
                word
            }
            else word.copy(body = word.body.lowercase())
        }

        return Sentence(words)
    }
}
package processors

import data_types.Document
import data_types.Fragment
import data_types.Line
import data_types.Sentence
import data_types.Word


class NameHarvester(
    private val not_names: Set<String> = setOf("the", "of", "a")
) {
    fun run(tokens: List<Word>): Set<String>
    {
        val high_confidence_names = mutableSetOf<String>()

        tokens
//            .filterIsInstance<Word>()
            .forEach { word ->
                if (word.isNameCase() && word.body.lowercase() !in not_names)
                {
                    if (!word.is_sentence_start)
                    {
                        high_confidence_names.add(word.body)
                    }
                }
            }

        return high_confidence_names
    }


    fun run(doc: Document): Set<String>
    {
        val names = mutableSetOf<String>()

        doc.fragments.forEach { fragment ->
            names.addAll(this.run(fragment))
        }

        return names
    }


    fun run(fragment: Fragment): Set<String>
    {
        val names = mutableSetOf<String>()

        fragment.lines.forEach { line ->
            names.addAll(this.run(line))
        }

        return names
    }


    fun run(line: Line): Set<String>
    {
        val names = mutableSetOf<String>()

        line.sentences.forEach { sentence ->
            if (!sentence.words.first().head.contains('#'))
            {
                names.addAll(this.run(sentence))
            }
        }

        return names
    }


    fun run(sentence: Sentence): Set<String>
    {
        val names = mutableSetOf<String>()

        sentence.words.forEachIndexed { index, word ->
            if (index > 0 && word.isNameCase() && word.body.lowercase() !in not_names)
            {
                if (!word.is_sentence_start)
                {
                    names.add(word.bodyWithoutMarkers())
                }
            }
        }

        return names
    }
}
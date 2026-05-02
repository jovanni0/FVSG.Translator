import processors.CaseProcessor
import processors.DocumentSplitter
import processors.NameHarvester
import processors.PostProcessor
import processors.PreProcessor
import processors.PunctuationProcessor



class Pipeline
{
    val pre_processor = PreProcessor()
    val doc_splitter = DocumentSplitter()
    val name_harveser = NameHarvester()
    val punctuation_processor = PunctuationProcessor()
    val case_processor = CaseProcessor()
    val post_processor = PostProcessor()


    fun transform(markdown: String): String
    {
        val processed = pre_processor.run(markdown)

        var doc = doc_splitter.splitDoc(processed)

        val names = name_harveser.run(doc)
        case_processor.setNames(names)

        doc = punctuation_processor.run(doc)
        doc = case_processor.run(doc)
        doc = post_processor.run(doc)

        val doc_as_string = doc.toString()

        return doc_as_string
    }
}
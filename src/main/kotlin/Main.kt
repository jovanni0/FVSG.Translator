import processors.DocumentSplitter
import processors.NameHarvester
import java.io.File


fun main()
{
    val folderPath = "/home/jovanni/Downloads/java-output"
    val folder = File(folderPath)

    if (!folder.isDirectory)
    {
        println("Not a valid folder: $folderPath")
        return
    }

    val docSplitter = DocumentSplitter()
    val nameHarvester = NameHarvester()

    folder.listFiles { file -> file.extension.lowercase() == "md" }?.forEach { mdFile ->
        println("Processing: ${mdFile.name}")

        val content = mdFile.readText()
        val lines = docSplitter.splitDoc(content)
        val names = nameHarvester.run(lines)

        // Create a .txt file with the same name
        val names_output_file = File(mdFile.parentFile, mdFile.nameWithoutExtension + ".txt")
        names_output_file.bufferedWriter().use { writer ->
            names.forEach { writer.write(it + "\n") }
        }

        val pipeline = Pipeline()
        val process_result = pipeline.transform(content)

        val content_output_file = File(mdFile.parentFile, mdFile.nameWithoutExtension + "-processed.txt")
        content_output_file.bufferedWriter().use { writer -> writer.write(process_result) }

        println("Exported names to: ${names_output_file.name}")
    }
}
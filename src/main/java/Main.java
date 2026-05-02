import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import static kotlin.io.ConsoleKt.readln;



public class Main
{
    public static void main(String[] args)
    {
        if (args.length < 2)
        {
            System.out.println("Usage: java EpubToMarkdown <input_epub> <output_dir>");
            System.exit(1);
        }

        Path epubPath = Paths.get(args[0]).toAbsolutePath();
        Path outputDir = Paths.get(args[1]).toAbsolutePath();

        try
        {
            confirmAndClearDir(outputDir);
            Files.createDirectories(outputDir);

            List<String> markdown_files = EpubExtractor.processEpubFile(epubPath, outputDir);
            Pipeline pipeline = new Pipeline();

            System.out.print("Processing chapter: ");
            for (int index = 0; index < markdown_files.size(); index++)
            {
                String file = markdown_files.get(index);

                System.out.print(index + ", ");
                String fvsg_markdown = pipeline.transform(file);

                saveToFile(fvsg_markdown, outputDir, index);
            }

            System.out.println();
            System.out.println("Extraction complete");
        }
        catch (Exception e)
        {
            System.err.println("Fatal error: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Done!");
    }


    private static void saveToFile(String content, Path outputDir, int index)
            throws IOException
    {
        String fileName = String.format("chapter_%03d.md", index);
        Path filePath = outputDir.resolve(fileName);
        Files.writeString(filePath, content, StandardCharsets.UTF_8);
    }


    private static void confirmAndClearDir(Path dir)
            throws Exception
    {
        if (!Files.exists(dir))
        {
            return;
        }

        System.out.print("Directory '" + dir.getFileName() + "' exists. Delete it? [y/N]: ");
        var response = readln();

        if (!response.equalsIgnoreCase("y"))
        {
            throw new Exception("Directory " + dir.getFileName() + " already exists.");
        }

        deleteDirectoryRecursively(dir);
        System.out.println("Directory deleted.");
    }

    private static void deleteDirectoryRecursively(Path path)
            throws IOException
    {
        Files
            .walk(path)
            .sorted((a, b) -> b.compareTo(a)) // Delete files before folders
            .forEach(p -> {
                try { Files.delete(p); } catch (IOException e) { /* ignore */ }
            });
    }
}

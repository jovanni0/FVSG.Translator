import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubReader;
import com.vladsch.flexmark.html2md.converter.FlexmarkHtmlConverter;
import com.vladsch.flexmark.util.data.MutableDataSet;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;


public class EpubExtractor
{
    public static List<String> processEpubFile(Path epubPath, Path outputDir)
            throws IOException
    {
        InputStream is = Files.newInputStream(epubPath);
        Book book = new EpubReader().readEpub(is);

        MutableDataSet options = new MutableDataSet();
        options.set(FlexmarkHtmlConverter.OUTPUT_ATTRIBUTES_ID, false);
        options.set(FlexmarkHtmlConverter.SKIP_ATTRIBUTES, true);
        options.set(FlexmarkHtmlConverter.TYPOGRAPHIC_QUOTES, false);

        List<String> markdown_files = new ArrayList<>();
        for (Resource resource : book.getContents())
        {
            if (resource.getMediaType().getName().equals("application/xhtml+xml"))
            {
                String html = new String(resource.getData(), StandardCharsets.UTF_8);
                String markdown = FlexmarkHtmlConverter.builder(options).build().convert(html);

                markdown = markdown
                        .replace("—", " -- ")
                        .replace("---", " -- ")
                        .replaceAll("\\\\\\*\\s*\\\\\\*\\s*\\\\\\*", "---")
                        .replaceAll("[\\u00A0 ]+", " ")
                        .replace("  ", " ")
                        .replace("...", "…")
                        .replace("* ’", "*’")
                        .trim();

                markdown_files.add(markdown);
            }
        }

        return markdown_files;
    }
}
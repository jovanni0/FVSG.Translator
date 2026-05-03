package processors

/**
 * processes the Markdown content before it goes into the doc splitter.
 *
 * the changes are of type find-replace and aimed at resolving systematic problems in text that do not require complex
 * logic. they are:
 * - ellipses: in regular print, ellipsis are usually formed using periods, so they need to be condensed into the
 *             ellipsis character.
 */
class PreProcessor
{
    fun run(doc: String): String
    {
        val doc = ellipsis(doc)
        return ellipsisSeparation(doc)
    }


    /**
     * handles the replacements related to spread ellipsis (aka ellipsis condensation).
     *
     * these are:
     * - type 1 = spread ellipsis at the end of a sentence. in this case all spaces around are collapsed and the ellipsis
     *            is moved at the end of the previous word.
     *     - 1A = the SE have a space at the beginning and the end: `strokes . . . ”`
     *     - 1B = the SE have a space at the start: `strokes . . .”`
     * - type 2 = spread ellipsis at the start of a sentence. because it is a symbolic placeholder for previous words,
     *            it reteins the space after and only the space before is collapsed.
     *     - 2A = the SE have a space at the beginning and the end: `“ . . . we're`
     *     - 2B = the SE have a space at the end: `“. . . we're`
     * - type 3 = generic spread ellipsis in the text. because there is no context about the position in text, it's assumed
     *            they will be collapsed at the end of the previous word. the trainiling space case is ignored.
     *     - 3A-Q = the SE have space at the beggining + end and a question mark: `this . . . ?`
     *     - 3A-E = the SE have space at the beggining + end and a exclamation mark: `this . . . !`
     *     - 3A = the SE have a space at the beginning: `text . . .`
     *     - 3B = the SE are not padded: `text. . .`
     */
    private fun ellipsis(input: String): String
    {
        return input
            .replace(" . . . ”", "…”")  // 1A
            .replace(" . . .”", "…”")   // 1B
            .replace("“ . . .", "“…")   // 2A
            .replace("“. . .", "“…")    // 2B
            .replace(" . . . ?", "…?")     // 3A-Q
            .replace(" . . . !", "…!")     // 3A-E
            .replace(" . . .", "…")     // 3A
            .replace(". . .", "…")      // 3B
    }

    private fun ellipsisSeparation(input: String): String
    {
        return input
            .replace("…“", "… “")
            .replace(Regex("…([a-zA-Z])"), "… $1")
    }
}
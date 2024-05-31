package ayds.songinfo.moredetails.fulllogic.presentation

import ayds.songinfo.moredetails.fulllogic.domain.Card
import java.util.Locale

interface CardDescriptionHelper {
    fun getDescription(card: Card): String
}

private const val HEADER = "<html><div width=400><font face=\"arial\">"
private const val FOOTER = "</font></div></html>"

private const val LOCAL_MARKER = "[*]"

internal class CardDescriptionHelperImpl : CardDescriptionHelper {

    override fun getDescription(card: Card): String {
        val text = getText(card)
        return textToHtml(text, card.artistName)
    }

    private fun getText(card: Card): String {
        val prefix = if (card.isLocallyStored) LOCAL_MARKER else ""
        val text = card.text
        return "$prefix$text"
    }

    private fun textToHtml(text: String, term: String): String {
        val builder = StringBuilder()
        builder.append(HEADER)
        val textWithBold = text
            .replace("'", " ")
            .replace("\\n", "\n")
            .replace("\n", "<br>")
            .replace(
                "(?i)$term".toRegex(),
                "<b>" + term.uppercase(Locale.getDefault()) + "</b>"
            )
        builder.append(textWithBold)
        builder.append(FOOTER)
        return builder.toString()
    }
}
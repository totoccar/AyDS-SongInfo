package ayds.songinfo.moredetails.fulllogic.domain

data class Card(
    val artistName: String,
    val text: String,
    val url: String,
    val source: CardSource,
    var isLocallyStored: Boolean = false
)

enum class CardSource {
    LAST_FM
}
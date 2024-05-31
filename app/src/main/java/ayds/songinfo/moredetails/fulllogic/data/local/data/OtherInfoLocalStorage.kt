package ayds.songinfo.moredetails.fulllogic.data.local.data

import ayds.songinfo.moredetails.fulllogic.domain.Card
import ayds.songinfo.moredetails.fulllogic.domain.CardSource

interface OtherInfoLocalStorage {
    fun getCard(artistName: String): Card?
    fun insertCard(card: Card)
}

internal class OtherInfoLocalStorageImpl(
    private val cardDatabase: CardDatabase,
) : OtherInfoLocalStorage {

    override fun getCard(artistName: String): Card? {
        val artistEntity = cardDatabase.CardDao().getCardByArtistName(artistName)
        return artistEntity?.let {
            Card(
                artistName,
                artistEntity.content,
                artistEntity.url,
                CardSource.entries[artistEntity.source]
            )
        }
    }

    override fun insertCard(card: Card) {
        cardDatabase.CardDao().insertCard(
            CardEntity(
                card.artistName, card.text, card.url, card.source.ordinal
            )
        )
    }
}
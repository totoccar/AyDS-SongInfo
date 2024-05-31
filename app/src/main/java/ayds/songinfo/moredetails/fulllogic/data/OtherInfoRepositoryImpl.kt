package ayds.songinfo.moredetails.fulllogic.data
import ayds.songinfo.moredetails.fulllogic.data.local.data.OtherInfoLocalStorage
import ayds.artist.external.lastfm.data.LastFmBiography
import ayds.artist.external.lastfm.data.LastFmService
import ayds.songinfo.moredetails.fulllogic.domain.OtherInfoRepository
import ayds.songinfo.moredetails.fulllogic.domain.Card
import ayds.songinfo.moredetails.fulllogic.domain.CardSource

internal class OtherInfoRepositoryImpl(
    private val otherInfoLocalStorage: OtherInfoLocalStorage,
    private val lastFmService: LastFmService,
) : OtherInfoRepository {

    override fun getCard(artistName: String): Card {
        val dbCard = otherInfoLocalStorage.getCard(artistName)

        val card: Card

        if (dbCard != null) {
            card = dbCard.apply { markItAsLocal() }
        } else {
            card = lastFmService.getArticle(artistName).toCard()
            if (card.text.isNotEmpty()) {
                otherInfoLocalStorage.insertCard(card)
            }
        }
        return card
    }

    private fun Card.markItAsLocal() {
        isLocallyStored = true
    }
}

private fun LastFmBiography.toCard() =
    Card(artistName, biography, articleUrl, CardSource.LAST_FM)